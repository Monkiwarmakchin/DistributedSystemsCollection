import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.io.PrintStream;

class Chat
{
	static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException
	{
		DatagramSocket socket = new DatagramSocket();
		socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
		socket.close();
	}

	static byte[] recibe_mensaje_multicast(MulticastSocket socket, int logitud_mensaje) throws IOException
	{
		byte[] buffer = new byte[logitud_mensaje];
		DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
		socket.receive(paquete);
		return paquete.getData();
	}

	static class Worker extends Thread
	{
		public void run()
		{
			try
	        {
	        	//Conectamos al hilo cliente al grupo 230.0.0.0 y al puerto 50000.
	        	InetAddress grupo = InetAddress.getByName("230.0.0.0");
	        	MulticastSocket socket = new MulticastSocket(50000);
				socket.joinGroup(grupo);
				System.out.print("\n");

	        	//Ciclo infinito del hilo Cliente para recibir mensajes.
		    	for(;;)
		    	{
		    		System.out.print("Ingrese el mensaje a enviar: ");
		    		byte[] a = recibe_mensaje_multicast(socket, 100);//Maximo 100 caracteres (contando el nombre y menos 28 espacios)

		    		//NOTA IMPORTANTE: Para la correcta impresión de los caracteres especiales en windows,
		    		//la instancia de la terminal de comandos (cmd.exe) en donde se ejecute este programa
		    		//debe tener como pagina de codigos activa la "1252 (ANSI - Latin I)".
		    		//Para esto se puede usar el comando "chcp 1252" en la propia linea de comandos,
		    		//dicho comando solo hace el cambio de pagina de codigos temporalmente,
		    		//pues al abrir cualquier otra instancia de la terminal esta tendra su valor por defecto,
		    		//esto implica que el cambio de pagina se debe hacer por cada instancia de la que se utilice. 
    				System.out.println(new String(a,"ISO-8859-1"));
		    	}

			}
		    catch (Exception e)
		    {
		        System.err.println(e.getMessage());
		    }
		}
	}

	public static void main(String[] args) throws Exception
	{
		if(args.length != 1)
		{
			System.err.println("\n***Ingrese su nombre de usuario como primer argumento***\n");
			System.exit(0);
		}

		Worker w = new Worker();
		w.start();

		String nombre = args[0];

		Scanner entrada = new Scanner(System.in);

		//Ciclo infinito del hilo Sevidor para enviar mensajes.
		for(;;)
		{
			String mensaje = entrada.nextLine();
			String b = "\r" + nombre + ": " + mensaje + "                            ";
			envia_mensaje_multicast(b.getBytes(),"230.0.0.0",50000);
		}
	}
}