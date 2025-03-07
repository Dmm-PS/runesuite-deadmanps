package wind.net.login;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import wind.Config;
import wind.Punishments;
import wind.Server;
import wind.model.players.Client;
import wind.model.players.PlayerHandler;
import wind.model.players.saving.PlayerSave;
import wind.net.ConnectionHandler;
import wind.net.PacketBuilder;
import wind.util.ISAACCipher;
import wind.util.Misc;

public class RS2LoginProtocol extends FrameDecoder {

//	private static final BigInteger RSA_MODULUS = new BigInteger("99318708405225540704919151682347795615878504311589890657729205431710912029818884231013212195234069446414819888839361407528134357252619787011684988022302648054936681453896565686196508803676251811351350491661667497378665241546781622429965024630505890173571513770884458623488248534153337379289980513862896356533");

 //  private static final BigInteger RSA_EXPONENT = new BigInteger("67143975076703584333615330796925422777318354700845349886039186655742308442454727630792855631506487646563849581075068228969002560880641046787916979405772925628557160042396472446072099952149009201279260446723338242263961206622702032912194697564620773202155794368531263937130624994663182611285442952391621166553");


	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 2)
				return null;
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			buffer.readUnsignedByte();
			channel.write(new PacketBuilder().putLong(0).put((byte) 0)
					.putLong(new SecureRandom().nextLong()).toPacket());
			state = LOGGING_IN;
			return null;
		case LOGGING_IN:
			@SuppressWarnings("unused")
			int loginType = -1,
			loginPacketSize = -1,
			loginEncryptPacketSize = -1;
			if (2 <= buffer.capacity()) {
				loginType = buffer.readByte() & 0xff; // should be 16 or 18
				loginPacketSize = buffer.readByte() & 0xff;
				loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
				if (loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
					System.out.println("Zero or negative login size.");
					channel.close();
					return false;
				}
			}

			/**
			 * Read the magic id.
			 */
			if (loginPacketSize <= buffer.capacity()) {
				int magic = buffer.readByte() & 0xff;
				int version = buffer.readUnsignedShort();
				if (magic != 255) {
					System.out.println("Wrong magic id.");
					channel.close();
					return false;
				}
				if (version != 1) {
					// Dont Add Anything
				}
				@SuppressWarnings("unused")
				int lowMem = buffer.readByte() & 0xff;

				/**
				 * Pass the CRC keys.
				 */
				for (int i = 0; i < 9; i++) {
					buffer.readInt();
				}
				loginEncryptPacketSize--;
				if (loginEncryptPacketSize != (buffer.readByte() & 0xff)) {
					System.out.println("Encrypted size mismatch.");
					channel.close();
					return false;
				}

				/**
				 * Our RSA components.
				 */
				ChannelBuffer rsaBuffer = buffer.readBytes(loginEncryptPacketSize);
		//		byte[] encryptionBytes = new byte[loginEncryptPacketSize];
				BigInteger bigInteger = new BigInteger(rsaBuffer.array());
				bigInteger = bigInteger.modPow(RS2Decoder.serverExponent, RS2Decoder.serverModulus);
				System.out.println("Using SE: "+RS2Decoder.serverExponent);
				System.out.println("Using SM: "+RS2Decoder.serverModulus);
				rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
				//Client cl = new Client(channel, -1);
				String ip = channel.getRemoteAddress().toString().replaceAll("/", "").split(":")[0];
				if ((rsaBuffer.readByte() & 0xff) != 10) {
					System.out.println("Encrypted id != 10. From: [" + ip + "]");
					channel.close();
					return false;
				}
				final long clientHalf = rsaBuffer.readLong();
				final long serverHalf = rsaBuffer.readLong();
				int uid = rsaBuffer.readInt();
				
				if (uid == 99735086) {
					System.out.println("[SECURITY] Cheat Client Detected From: [" + ip + "]");
					channel.close();
					return false;
				}
				final String name = Misc.formatPlayerName(Misc
						.getRS2String(rsaBuffer));
				final String pass = Misc.getRS2String(rsaBuffer);

				final int[] isaacSeed = { (int) (clientHalf >> 32),
						(int) clientHalf, (int) (serverHalf >> 32),
						(int) serverHalf };
				final ISAACCipher inCipher = new ISAACCipher(isaacSeed);
				for (int i = 0; i < isaacSeed.length; i++)
					isaacSeed[i] += 50;
				final ISAACCipher outCipher = new ISAACCipher(isaacSeed);
				// final int version = buffer.readInt();
				channel.getPipeline().replace("decoder", "decoder",
						new RS2Decoder(inCipher));
				return login(channel, inCipher, outCipher, version, name, pass);
			}
		}
		return null;
	}

	private static Client login(Channel channel, ISAACCipher inCipher,
			ISAACCipher outCipher, int version, String name, String pass) {
		int returnCode = 2;
		String ip = channel.getRemoteAddress().toString().replaceAll("/", "").split(":")[0];
		if (!name.matches("[A-Za-z0-9 ]+") || name.length() > 12) { 
			returnCode = 8;
		} else {
			returnCode = ConnectionHandler.evaluate(ip);
		}
		Client cl = new Client(channel, -1);
		cl.playerName = name;
		cl.playerName2 = cl.playerName;
		cl.playerPass = pass;
		cl.connectedFrom = ip;
		cl.outStream.packetEncryption = outCipher;
		cl.saveCharacter = false;
		cl.isActive = true;
	//	if (cl.getX() == -1 || cl.getY() == -1) {
		//	cl.getPA().movePlayer(3218, 3220, 0);
		//}

		if (Punishments.isNamedBanned(cl.playerName)) {
			returnCode = 4;
		}
		/*if (Censor.isInvalid(name)) {
			returnCode = 3;
		}*/
		
		if (PlayerHandler.isPlayerOn(name)) {
			returnCode = 5;
		}
		if (PlayerHandler.getPlayerCount() >= Config.MAX_PLAYERS) {
			returnCode = 7;
		}
		if (Server.UpdateServer) {
			returnCode = 14;
		}
		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
			if (load == 0)
				cl.addStarter = true;
			if (load == 3) {
				returnCode = 3;
				cl.saveFile = false;
			} else {
				for (int i = 0; i < cl.playerEquipment.length; i++) {
					if (cl.playerEquipment[i] == 0) {
						cl.playerEquipment[i] = -1;
						cl.playerEquipmentN[i] = 0;
					}
				}
				if (!Server.playerHandler.newPlayerClient(cl)) {
					returnCode = 7;
					cl.saveFile = false;
				} else {
					cl.saveFile = true;
				}
			}
		}
		if (returnCode == 2) {
			cl.saveCharacter = true;
			cl.packetType = -1;
			cl.packetSize = 0;
			final PacketBuilder bldr = new PacketBuilder();
			bldr.put((byte) 2);
			bldr.put((byte) cl.getRights().getProtocolValue());
			bldr.put((byte) 0);
			channel.write(bldr.toPacket());
		} else {
			System.out.println("returncode:" + returnCode);
			sendReturnCode(channel, returnCode);
			return null;
		}
		synchronized (PlayerHandler.lock) {
			cl.initialize();
			cl.initialized = true;
		}
		return cl;
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket())
				.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(final ChannelFuture arg0)
							throws Exception {
						arg0.getChannel().close();
					}
				});
	}

}
