
package ficheroaccesomultiplesincro;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FicheroAccesoMultipleSincro 
{
    private static final Logger LOGGER = Logger.getLogger("com.prueba.hector");
    public static void main(String[] args) throws IOException 
    {
        RandomAccessFile raf =null;       
        FileLock bloqueo;
        FileChannel canal;
        FileWriter escribe;
        PrintWriter pw;
        String nombrearchivo="";
        File archivo = null;
        int dato=0;
        int orden =0;
        FileHandler fh = null;
        bloqueo= null;
        
        
        try{
        fh = new FileHandler("MyLogFile.log",true);
        SimpleFormatter formater = new SimpleFormatter();
        fh.setFormatter (formater);
        }
        catch (IOException ex)
        {
            System.out.println("No se ha podido abrir el fichero Log.");
            System.exit(-1);
        }
        LOGGER.addHandler(fh);
        LOGGER.setLevel(Level.ALL);
        
       try
       {
           if (args.length >0)
        {
            orden = Integer.parseInt(args[0]);
        }
       }
       catch (NumberFormatException ex)
       {
            LOGGER.log(Level.WARNING, "Error de acceso al fichero " + orden, ex);
       }
        if (args.length >1)
        {
            orden = Integer.parseInt(args[1]);
        }
        else
        {
            nombrearchivo ="prueba.txt";
        }
        
        //Preparo el acceso
        archivo = new File(nombrearchivo);
        try{
            
        raf = new RandomAccessFile(archivo, "rwd");
        
        // Bloqueo
        bloqueo = raf.getChannel().lock();
        raf.seek(0);
        if (raf.length() ==0)
        {
            dato=0;
        }
        else 
        {
            dato =raf.readInt();
        }
        System.out.println("Dato leido: " + dato);
        dato++;
        raf.seek(0);
        raf.writeInt(dato);
        bloqueo.release();
        bloqueo = null;
        raf.close();
        //canal.close();
        }
         catch (FileNotFoundException ex)
        {
            LOGGER.log(Level.SEVERE, "Error de accesi ak fichero " + orden, ex);
            
        }
        
        finally{
            try {
                if (null!= bloqueo)
                {
                    bloqueo.release();
                }
                if (null!=raf)
                {
                    raf.close();
                    
                }
            
            }
            catch(Exception e2)
            {
                LOGGER.log(Level.SEVERE,"Error cerrar fichero",e2);
            }
        }
      }
    
}
