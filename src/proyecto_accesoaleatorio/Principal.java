package proyecto_accesoaleatorio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

/**
 * Ejemplos de acceso aleatorio a un archivo. Basado en los ejemplos de la
 * documentación oficial de Oracle:
 * https://docs.oracle.com/javase/tutorial/essential/io/index.html
 *
 * Referencia teórica: Página 31 de Proydesa.
 *
 * @author Angonoa, Franco
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            randomAccessFiles();
        } catch (IOException io) {
            System.err.printf("Error de tipo IO: %s%n", io);
        }
    } //Fin del metodo main()

    /**
     * Archivos de acceso aleatorio.
     *
     * Los archivos de acceso aleatorio permiten accederaleatoria y no
     * secuencialmente al contenido de un archivo.
     *
     * Para acceder a un archivo aleatoriamente, abra el archivo, busque una
     * ubicación concreta y lea desde el archivo o escriba en él.
     *
     * La funcionalidad de acceso aleatorio se activa mediante la interfaz
     * SeekableByteChannel.
     *
     * La clase FileChannel implanta la interfaz SeekableByteChannel.
     *
     * @throws IOException
     */
    public static void randomAccessFiles() throws IOException {
        crearDirectorio();
        crearArchivo();

        Path file = Paths.get(".\\carpetaAccesoAleatorio\\archivoAccAleatorio.txt");
        String s = "¡Estoy aquí!\n";
        byte data[] = s.getBytes();
        ByteBuffer out = ByteBuffer.wrap(data);

        ByteBuffer copy = ByteBuffer.allocate(12); // Lee los primeros 12 bytes del archivo.

        try (FileChannel fc = (FileChannel.open(file, READ, WRITE))) {
            int nread;
            do {
                nread = fc.read(copy);
            } while (nread != -1 && copy.hasRemaining());

            // Escribe "¡Estoy aquí!" al principio del archivo.
            fc.position(0);
            while (out.hasRemaining()) {
                fc.write(out);
            }
            out.rewind();

            // Se mueve al final del archivo. Copia los primeros 12 bytes
            // al final del archivo.  Luego escribe otra vez "¡Estoy aquí!".
            long length = fc.size();
            fc.position(length - 1);
            copy.flip();
            while (copy.hasRemaining()) {
                fc.write(copy);
            }
            while (out.hasRemaining()) {
                fc.write(out);
            }
        }
    } //Fin del metodo randomAccessFile()

    public static void crearDirectorio() throws IOException {
        Path directorio = Paths.get(".\\carpetaAccesoAleatorio");

        if (!Files.isDirectory(directorio)) {
            Files.createDirectory(directorio);
        }
    } //Fin del metodo crearDirectorio()

    public static void crearArchivo() throws IOException {
        Path file = Paths.get(".\\carpetaAccesoAleatorio\\archivoAccAleatorio.txt");
        if (!Files.isRegularFile(file)) {
            Files.createFile(file);
        }

        Charset charset = Charset.forName("UTF-8");
        String s = "ARCHIVO INICIAL EN MAYUSCULAS A MODO DE EJEMPLO";

        //Utilizamos el try with resource ya que BufferedWriter es una clase que implanta AutoCloseable
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
        }
    } //Fin del metodo crearArchivo()

} //Fin de la clase
