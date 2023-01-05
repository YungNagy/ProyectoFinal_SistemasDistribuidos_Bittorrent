
package Bittorrent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;



public class PeerTorrentCliente {
    
    DataOutputStream mandarMensajeServer;
    FileInputStream datos;
    ObjectInputStream entradaobjeto = null;
    ObjectOutputStream salidaobjeto = null;
    FileOutputStream lugardestino = null;
    BufferedOutputStream archivosalida= null;
    BufferedInputStream archivoentrada = null;
    
    boolean in = false;
    int tamano = 0;
    String ArchivoOb;
    String IpTracker="localhost";
    String DirecTorrent;
    int puertotrack=4500;
    Scanner scan = new Scanner(System.in);
    

    public static void main(String[] args) {
        PeerTorrentCliente client = new PeerTorrentCliente();
        client.conectar();
    }
    public void conectar(){
    //Ip y puerto por defecto
    String arch;
    try{
        System.out.println("Inserte la direccion del torrent a enviar para obtener el archivo deseado: ");
        DirecTorrent=scan.next();
        System.out.println("Conexion a la direccion ip: "+obtenerIpTrackerTorrent(DirecTorrent)+" en el puerto: "+obtenerPuertoTrackerTorrent(DirecTorrent));
        //Abrimos el socket
        Socket conexion = new Socket(obtenerIpTrackerTorrent(DirecTorrent),obtenerPuertoTrackerTorrent(DirecTorrent));
        DataOutputStream mensajesalida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream mensajeentrada = new DataInputStream(conexion.getInputStream());
        mandarMensajeServer = mensajesalida;
        //System.out.println("Inserte el nombre del archivo a obtener: ");
        //arch = scan.next();
        arch = enviarNombreArchivoTorrent(DirecTorrent);
        //Mandamos el nombre del archivo que queremos obtener al servidor
        mandarMensajeServer.writeUTF(arch);
        //Obtenemos la respuesta del servidor 
        in = mensajeentrada.readBoolean();
        if(in == true ){
        System.out.println("Mensaje recibido del servidor: "+mensajeentrada.readUTF());
        System.out.println("Mensaje recibido del servidor: "+mensajeentrada.readUTF());
        tamano=mensajeentrada.readInt();
        ArchivoOb = mensajeentrada.readUTF();
        
        //System.out.println("Escoga el destino para recuperar el archivo: ");
        //String rutaDestino = scan.next();
        lugardestino = new FileOutputStream("archivos\\Descarga_"+ArchivoOb); 
        archivosalida = new BufferedOutputStream(lugardestino);
        archivoentrada = new BufferedInputStream(conexion.getInputStream());
        //Procedemos a realizar la apertura del archivo hacia el servidor
            byte[] buffer = new byte[tamano];

              
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) archivoentrada.read();
                }
        
        archivosalida.write(buffer);
        archivosalida.flush();
        //ESPACIO
        ConsArchivo();
        archivoentrada.close();
        archivosalida.close();
        conexion.close();
        }else {
                System.out.println("Mensaje del Servidor: " + mensajeentrada.readUTF());
                recuperarconectar();
            }
    }catch(Exception e){
        System.out.println("Error en la conexion"+e.getMessage());
        //Continuamos a recuperar la conexion con el servidor  
        recuperarconectar();
    }
    }
    
    public void ConsArchivo(){
        Socket socket2;
        ServerSocket socketserver1;
        TrackerTorrentServidor.usar conservidor;
        int id2=0;
        try{
        socketserver1 = new ServerSocket(puertotrack);
        while(true){
        
        System.out.println("Socket abierto en el puerto: "+puertotrack);
        socket2= socketserver1.accept();
        id2++;
        System.out.println("\nSe conecto el cliente con ID: " + id2 + " desde la IP: " + socket2.getInetAddress());
        
        conservidor = new TrackerTorrentServidor.usar(socket2, id2);
        conservidor.start();  
        }
    
        }catch(IOException e){

        
        }finally{}
    }
    
    public void recuperarconectar(){
  //Ip y puerto por defecto
    String arch;
    try{
        System.out.println("Inserte la direccion del torrent a enviar para obtener el archivo deseado: ");
        DirecTorrent=scan.next();
        System.out.println("Conexion a la direccion ip:"+obtenerIpTrackerTorrent(DirecTorrent)+" en el puerto: "+obtenerPuertoTrackerTorrent(DirecTorrent));
        //Abrimos el socket
        Socket conexion = new Socket(obtenerIpTrackerTorrent(DirecTorrent),obtenerPuertoTrackerTorrent(DirecTorrent));
        DataOutputStream mensajesalida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream mensajeentrada = new DataInputStream(conexion.getInputStream());
        mandarMensajeServer = mensajesalida;
        //System.out.println("Inserte el nombre del archivo a obtener: ");
        //arch = scan.next();
        arch = enviarNombreArchivoTorrent(DirecTorrent);
        //Mandamos el nombre del archivo que queremos obtener al servidor
        mandarMensajeServer.writeUTF(arch);
        //Obtenemos la respuesta del servidor 
        in = mensajeentrada.readBoolean();
        if(in == true ){
        System.out.println("Mensaje recibido del servidor: "+mensajeentrada.readUTF());
        System.out.println("Mensaje recibido del servidor: "+mensajeentrada.readUTF());
        tamano=mensajeentrada.readInt();
        ArchivoOb = mensajeentrada.readUTF();
        
        //System.out.println("Escoga el destino para recuperar el archivo: ");
        //String rutaDestino = scan.next();
        lugardestino = new FileOutputStream("archivos\\Descarga_"+ArchivoOb); 
        archivosalida = new BufferedOutputStream(lugardestino);
        archivoentrada = new BufferedInputStream(conexion.getInputStream());
        //Procedemos a realizar la apertura del archivo hacia el servidor
            byte[] buffer = new byte[tamano];

              
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) archivoentrada.read();
                }
        
        archivosalida.write(buffer);
        archivosalida.flush();
        //ESPACIO
        ConsArchivo();
        archivoentrada.close();
        archivosalida.close();
        conexion.close();
        }else {
                System.out.println("Mensaje recibido del servidor: " + mensajeentrada.readUTF());
                conectar();
            }
    }catch(Exception e){
        System.out.println("Error en la conexion: "+e.getMessage());
        //Continuamos a volver a iniciar el proceso inicial   
        conectar();
    }
    }
    
    
    public String enviarNombreArchivoTorrent(String direccion){
    JSONParser  parser = new JSONParser();  
    String nombre=null;
    try{
         Object obj = parser.parse(new FileReader(direccion));
         JSONObject objetJ = new JSONObject(obj.toString());
         nombre = (String)objetJ.getString("name");
    }catch(Exception e){
    e.printStackTrace();
    }
    return nombre;
    }
    
    public String obtenerIpTrackerTorrent(String direccion){
    JSONParser  parser = new JSONParser();  
    String tracker=null;
    try{
         Object obj = parser.parse(new FileReader(direccion));
         JSONObject objetJ = new JSONObject(obj.toString());
         tracker = (String)objetJ.getString("tracker");
    }catch(Exception e){
    e.printStackTrace();
    }
    return tracker;
    }
    public Integer obtenerPuertoTrackerTorrent(String direccion){
    JSONParser  parser = new JSONParser();  
    int puerto=0;
    
    try{
         Object obj = parser.parse(new FileReader(direccion));
         JSONObject objetJ = new JSONObject(obj.toString());
         String pp = (String)objetJ.getString("puertoTracker");
         puerto = Integer.parseInt(pp);
    }catch(Exception e){
    e.printStackTrace();
    }
    return puerto;
    }
    
}
