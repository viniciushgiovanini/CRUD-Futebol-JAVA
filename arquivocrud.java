import java.util.Scanner;
import java.io.RandomAccessFile;

import java.io.PrintWriter;

public class arquivocrud {

  // -------------------FUNCAO-PARA-TESTAR-O-ARQUIVO----------------//

  public void deletaTudo() {

    try {

      PrintWriter writer = new PrintWriter("dados/futebol.db");
      writer.print("");
      writer.close();

    } catch (Exception e) {
      System.out.println("ERRO NO DELETA TUDO");
    }

  }

  // -------------------Create---------------------------------//

  public void escreverArquivo(fut ft) {

    /*
     * como ta sendo feita a escrita
     * ID COMECO DO ARQUIVO + Tam do Arquiv +
     * ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)
     */
    // Escrita no Arquivo
    RandomAccessFile arq;
    byte[] ba;

    try {
      // verificarArquivo("dados/futebol.db");
      short idcabecalhosave = 0;
      arq = new RandomAccessFile("dados/futebol.db", "rw");

      if (arq.length() == 0) {
        idcabecalhosave = ft.getIdClube();
        arq.writeShort(idcabecalhosave);

      }
      arq.seek(0);
      idcabecalhosave = arq.readShort();
      idcabecalhosave++;
      arq.seek(0);
      arq.writeShort(idcabecalhosave);

      // System.out.println(arq.getFilePointer());
      long finaldoarquivo = (long) arq.length();
      arq.seek(finaldoarquivo);
      // System.out.println(arq.getFilePointer());

      ft.setIdClube(idcabecalhosave);
      ba = ft.toByteArray();
      arq.writeInt(ba.length);
      arq.write(ba);

    } catch (Exception e) {
      String erro = e.getMessage();

      if (erro.contains("No such file or directory")) {

        System.out.println("Diretório do arquivo não encontrado !");
        return;
      }

    }

    System.out.println("------X------");
    System.out.println(ft.toString());

  }

  public void criarClube(Scanner entrada, fut ft) {

    String cnpjparaveri = null;

    System.out.print("Escreva o nome do clube: ");
    ft.setNome(entrada.nextLine());

    if (!(ft.getNome().equals(""))) {

      System.out.println();
      System.out.print("Insira o cnpj do clube: ");
      cnpjparaveri = entrada.nextLine();// AQUI PRECISA TRATAR O CPNJ;
      ft.setCnpj(cnpjparaveri);
      System.out.println();
      System.out.print("Insira a cidade do clube: ");
      ft.setCidade(entrada.nextLine());

      escreverArquivo(ft);
    } else {
      System.out.println("\nArquivo com o Campo nome vazio não é possivel ser escrito !\n");
      return;
    }

  }

  // -------------------Create - FIM---------------------------------//

  // ----------------------READ-------------------------//

  public long pesquisarNoArquivo(String entrada, fut ft2) {

    RandomAccessFile arq;
    String lapide = "";
    long posicaoRetorno = -1;
    boolean idOrnot = entrada.matches("-?\\d+");
    boolean idDeletado = false;
    boolean idencontrado = false;
    boolean idnexiste = false;

    if (idOrnot == true) {
      long posicaosave = 0;
      try {

        arq = new RandomAccessFile("dados/futebol.db", "rw");

        short idproc = Short.valueOf(entrada);
        arq.seek(2);
        posicaoRetorno = arq.getFilePointer();
        int tam = arq.readInt();
        posicaosave = arq.getFilePointer();
        short idlido = arq.readShort();
        int contador = 0;

        long ultimaPosiArq = (long) arq.length();

        while (contador <= idproc && idencontrado == false && idnexiste == false) {

          if (idlido == idproc) {

            lapide = arq.readUTF();
            idencontrado = true;
            if ((lapide.equals(" ") == true)) {
              idDeletado = false;
            } else {
              idDeletado = true;
            }

          }

          if ((idencontrado == false) && (posicaosave + tam < ultimaPosiArq) && (contador <= idproc)) {
            arq.seek(posicaosave);
            int converlt = (int) posicaosave;
            posicaosave = (long) tam + converlt;
            arq.seek(posicaosave);

            posicaoRetorno = arq.getFilePointer();
            tam = arq.readInt();
            posicaosave = arq.getFilePointer();
            idlido = arq.readShort();

          } else {

            if (idencontrado == false) {
              idnexiste = true;
            }

          }

          contador++;

        }
        arq.close();
      } catch (Exception e) {
        String erro = e.getMessage();

        if (erro.contains("No such file or directory")) {

          System.out.println("Diretório do arquivo não encontrado ! ERROR" + e.getMessage());
          return -10;
        }
      }

    } else {

      try {
        arq = new RandomAccessFile("dados/futebol.db", "rw");

        long tamTotalArq = arq.length();
        long posiI;
        long saveLapide;
        long posiMudar;
        Boolean estouro = false;
        arq.seek(2);
        posicaoRetorno = arq.getFilePointer();
        int tamRegistro = arq.readInt();
        posiI = arq.getFilePointer();
        arq.seek(arq.getFilePointer() + 2);
        saveLapide = arq.getFilePointer();
        arq.seek(saveLapide + 3);
        String nomeR = arq.readUTF();
        // arq.seek(posiI);

        while (estouro == false) {

          if (entrada.equals(nomeR) == true) {
            idencontrado = true;
            arq.seek(saveLapide);
            lapide = arq.readUTF();
            if (lapide.equals(" ")) {
              idDeletado = false;
              estouro = true;
            } else {
              idDeletado = true;
            }
          } else {
            idDeletado = true;
          }

          if (posiI + tamRegistro < tamTotalArq && (idDeletado != false) && (estouro == false)) {
            posiMudar = (long) tamRegistro;
            arq.seek(posiMudar + posiI);
            posicaoRetorno = arq.getFilePointer();
            tamRegistro = arq.readInt();
            posiI = arq.getFilePointer();
            arq.seek(arq.getFilePointer() + 2);
            saveLapide = arq.getFilePointer();
            arq.seek(saveLapide + 3);
            nomeR = arq.readUTF();
            arq.seek(posiI);
          } else {
            estouro = true;
          }

        }
        arq.close();
      } catch (Exception e) {
        String erro = e.getMessage();

        if (erro.contains("No such file or directory")) {

          System.out.println("Diretório do arquivo não encontrado ! ERROR" + e.getMessage());
          return -10;
        }
      }

    }

    if (idDeletado == true || idencontrado == false || idnexiste == true) {
      posicaoRetorno = -1;
    }

    return posicaoRetorno;

  }

  public long procurarClube(String recebendo, fut ft2) {

    /*
     * como ta sendo feita a escrita
     * ID COMECO DO ARQUIVO + Tam do Arquiv +
     * ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)
     */
    // Escrita no Arquivo

    long retornoPesquisa = pesquisarNoArquivo(recebendo, ft2);
    byte[] ba;
    RandomAccessFile arq;

    if (retornoPesquisa >= 0) {

      try {
        arq = new RandomAccessFile("dados/futebol.db", "rw");
        arq.seek(retornoPesquisa);
        int tamRegistro = arq.readInt();
        ba = new byte[tamRegistro];
        arq.read(ba);
        ft2.fromByteArray(ba);

      } catch (Exception e) {
        String erro = e.getMessage();

        if (erro.contains("No such file or directory")) {

          System.out.println("\nDiretório do arquivo não encontrado ! ERROR" + e.getMessage());
          return -10;
        }
      }
    } else {
      if (retornoPesquisa == -1) {

        System.out.println("\nRegistro Pesquisado não encontrado !\n");

      }
    }

    return retornoPesquisa;
  }

  // ----------------------READ - FIM-------------------------//

  public void arquivoDelete(String id, Scanner verificarultimoDelete, fut ft2) {

    RandomAccessFile arq;
    String lapide = "";
    boolean arquivoDeletado = false;
    try {
      arq = new RandomAccessFile("dados/futebol.db", "rw");

      long idExist = procurarClube(id, ft2);

      if (idExist >= 0) {

        System.out.println(ft2.toString());

        System.out.println("Você deseja deletar esse registro ?");
        String ultVeri = verificarultimoDelete.nextLine();

        if ((ultVeri.toLowerCase().equals("sim") == true)) {
          arq.seek(idExist + 6);
          lapide = "*";
          // System.out.println(arq.getFilePointer());
          arq.writeUTF(lapide);
          arquivoDeletado = true;
        } else {
          System.out.println("Registro não Deletado");
        }

      }

      else {
        System.out.println("Registro não Deletado !");
      }

    } catch (Exception e) {
      System.out.println("Erro quando foi deletar um registro" + e.getMessage());
    }

    if (arquivoDeletado == true) {

      System.out.println("Registro Deletado com Sucesso");

    }

  }

  // ----------------------Delete - FINAL-------------------------//

  // -----------------------UPDATE---------------------------------//

  // -----------------------UPDATE - FINAL---------------------------------//

}
