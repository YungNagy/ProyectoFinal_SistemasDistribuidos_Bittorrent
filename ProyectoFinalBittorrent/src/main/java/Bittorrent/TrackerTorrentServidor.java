
package Bittorrent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TrackerTorrentServidor {

    static class usar extends Thread{
    float ti=0,tf=0,tt=0,initTime=0;
    
    int cliente;
    Socket socket1=null;
    ObjectInputStream entrada;
    ObjectOutputStream salida;
    DataInputStream recvcliente;
    DataOutputStream sendcliente;
    File arch1=null;
    
    usar(Socket socket, int cliente){
        this.socket1 = socket;
        this.cliente = cliente;
    }
    //Verificamos si existe dentro de nuestra biblioteca
    boolean existe(String archivo){
        arch1 = new File("biblioteca\\"+archivo);
        Boolean res;
        res = arch1.exists()?true:false;
        return res;
    }
    //Para poder implementar los hilos en el servidor  
    public void run(){
    try{
    //Definimos las entradas y salidas para los clientes
    recvcliente = new DataInputStream(socket1.getInputStream());
    sendcliente = new DataOutputStream(socket1.getOutputStream());
    String arch = recvcliente.readUTF();
    //Procedemos a verificar si el archivo se encuentra en nuestra biblioteca
    if(existe(arch)==true){
        ti=(System.currentTimeMillis()-this.initTime);
        System.out.println("Enviando archivo torrent al cliente con ID de: "+cliente+"En un tiempo de: "+(System.currentTimeMillis()-this.initTime)+"Milisegundos");
        sendcliente.writeBoolean(true);
                    sendcliente.writeUTF("Archivo:" + arch + "existente la biblioteca del servidor");
                    sendcliente.writeUTF("Archivo con tamaño de: " + (arch1.length() / 1024) + " KB----Archivo" + arch1.getName());
                    sendcliente.writeInt((int) arch1.length());
                    sendcliente.writeUTF(arch);
                    System.out.println("Enviando archivo:" + arch + " a la direccion: " + socket1.getInetAddress());
                    FileInputStream in = new FileInputStream(arch1);
                    BufferedInputStream lectura = new BufferedInputStream(in);
                    
                    BufferedOutputStream salida = new BufferedOutputStream(socket1.getOutputStream()); 
                    byte[] arreglo = new byte[(int) arch1.length()];
                    lectura.read(arreglo);

                    for (int i = 0; i < arreglo.length; i++) {
                        salida.write(arreglo[i]);
                    }
                    tf=(System.currentTimeMillis() - this.initTime);
                    tt=tf-ti;
                    
                    System.out.println("Archivo Enviado a cliente:" + cliente);

                    System.out.println("El servidor termino el envio al cliente: " + cliente + "  tiempo de envio de:  "
                            + tt + " milisegundos");
                    System.out.println("Tiempo del cliente: "+cliente +": ("+(System.currentTimeMillis() - this.initTime)+") milisegundos");

                    salida.flush();
                    salida.flush();
                    salida.close();
                    in.close();
                }

                if (existe(arch) == false) {
                    sendcliente.writeBoolean(false);
                    sendcliente.writeUTF("Archivo: " + arch + " no se encuentra en la bilbioteca del servidor");
                    System.out.println("Respuesta enviada al cliente");
                }

    
    
    }catch(Exception ex){
    System.out.println("Error: "+ex.getMessage()+" Para el cliente con id: "+cliente);
    }finally {
                try {
                    if (salida != null) {
                        salida.close();
                    }
                    if (entrada != null) {
                        entrada.close();
                    }
                    if (socket1 != null) {
                        socket1.close();
                    }
                    System.out.println("Se termino el proceso con el cliente: "+cliente+" Ha recibido los archivos");

                } catch (Exception e) {
                    System.out.println("Mensaje erro:  "+e.getMessage());
                }
            }
    }
    }
   
    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket serversocket = null;
        usar ob;
        int puerto=4500;
        int id = 0;

        try {
            serversocket = new ServerSocket(puerto);
            while(true){
            
                System.out.println("Socket abierto en el puerto 4500 esperando conexiones con los clientes");
                socket = serversocket.accept();
                id++;
                System.out.println("\nSe conecto el cliente con el ID: " + id + " desde la IP: " + socket.getInetAddress());
                System.out.println("--------------------------------------------------------");
                ob = new usar(socket, id);
                ob.start();                
            }

        } catch (IOException e) {
            System.out.println(e.getMessage() + " Del servidor");
            System.exit(3);
        } finally {
        }
    }
    
}
