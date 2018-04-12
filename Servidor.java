import java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {

    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(9000); //cria um socket na porta 9000
        Socket sc = servidor.accept(); //aceita conexao

        if (sc.isConnected()) { //verifica conexao

            System.out.println("O cliente "+ sc.getInetAddress()+ " está conectado."); //mostra o cliente conectado

            //cria um BufferedReader de inputstream para receber os dados do cliente
            BufferedReader buffer = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            
            System.out.println("Requisição: ");

            String linha = buffer.readLine(); // le a primeira linha
            String[] dadosReq = linha.split(" "); //quebra a string
            String metodo = dadosReq[0];
            String caminhoArquivo = dadosReq[1];
            String protocolo = dadosReq[2];

            while (!linha.isEmpty()) { 

                System.out.println(linha);
                
                linha = buffer.readLine(); //le a proxima linha
            }
            
            if(caminhoArquivo.equals("/")){
                
                caminhoArquivo = "C:\\Users\\gisel\\Documents\\NetBeansProjects\\ServidorWeb\\src\\index.html";
            
            } 
           
            File arquivo = new File(caminhoArquivo);
            byte[] conteudo;
            String status = protocolo + " 200 OK\r\n";
            
            
            if (caminhoArquivo.equals("/"+arquivo.toString())){
                
                caminhoArquivo = "C:\\Users\\gisel\\Documents\\NetBeansProjects\\ServidorWeb\\src\\"+arquivo;

            
            } else {
                
                
                status = protocolo + " 404 Not Found\r\n"; //muda  status do protocolo
                arquivo = new File("C:\\Users\\gisel\\Documents\\NetBeansProjects\\ServidorWeb\\src\\404.html");
            }
            

            //lê todo o conteúdo do arquivo para bytes
            conteudo = Files.readAllBytes(arquivo.toPath());


            SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
            formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date data = new Date();
            String dataFormatada = formatador.format(data) + " GMT"; //padroniza a data

            String header = status //cabecalho de resposta HTTP
                + "Location: http://localhost:9000/\r\n"
                + "Date: " + dataFormatada + "\r\n"
                + "Server: MeuServidor/1.0\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + conteudo.length + "\r\n"
                + "Connection: close\r\n" + "\r\n";

            //canal de resposta
            OutputStream resposta = sc.getOutputStream();
            resposta.write(header.getBytes()); //escreve em bytes
            resposta.write(conteudo);
            resposta.flush(); //encerra

        }

    }
}