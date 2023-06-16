import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class C1000Byte
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    Socket conexion = new Socket("localhost",50000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    ByteBuffer b = ByteBuffer.allocate(1000*8);
    for(int i=1; i<=1000; i++)
    {
      b.putDouble(i);
    }
    byte[] a = b.array();
    long start = System.currentTimeMillis();
    salida.write(a);
    long end = System.currentTimeMillis();
    System.out.println("Tiempo: "+(end-start));

    salida.close();
    entrada.close();
    conexion.close();    
  }
}