package wind.net.login;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import wind.model.players.Client;
import wind.net.Packet;
import wind.net.Packet.Type;
import wind.util.ISAACCipher;

public class RS2Decoder extends FrameDecoder {

	public static BigInteger serverModulus;
	public static BigInteger serverExponent;
	public static BigInteger clientModulus;
	public static BigInteger clientExponent;
	 
	 
	    public static void genRSA(){
	        try {
	            KeyFactory factory = KeyFactory.getInstance("RSA");
	            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	            keyGen.initialize(1024);
	            KeyPair keypair = keyGen.genKeyPair();
	            PrivateKey privateKey = keypair.getPrivate();
	            PublicKey publicKey = keypair.getPublic();
	           
	            RSAPrivateKeySpec privSpec = factory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
	                       
	            RSAPublicKeySpec pubSpec = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);
	           
	            serverModulus=privSpec.getModulus();
	            serverExponent = privSpec.getPrivateExponent();
	            clientModulus=pubSpec.getModulus();
	            clientExponent = pubSpec.getPublicExponent();
	 
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }

	private final ISAACCipher cipher;

	private int opcode = -1;
	private int size = -1;

	public RS2Decoder(ISAACCipher cipher) {
		this.cipher = cipher;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (opcode == -1) {
			if (buffer.readableBytes() >= 1) {
				opcode = buffer.readByte() & 0xFF;
				opcode = (opcode - cipher.getNextValue()) & 0xFF;
				size = Client.PACKET_SIZES[opcode];
			} else {
				return null;
			}
		}
		if (size == -1) {
			if (buffer.readableBytes() >= 1) {
				size = buffer.readByte() & 0xFF;
			} else {
				return null;
			}
		}
		if (buffer.readableBytes() >= size) {
			final byte[] data = new byte[size];
			buffer.readBytes(data);
			final ChannelBuffer payload = ChannelBuffers.buffer(size);
			payload.writeBytes(data);
			try {
				return new Packet(opcode, Type.FIXED, payload);
			} finally {
				opcode = -1;
				size = -1;
			}
		}
		return null;
	}

}
