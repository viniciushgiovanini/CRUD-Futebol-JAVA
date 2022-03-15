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
      boolean arqExist = false;
      short idcabecalhosave = 0;
      arq = new RandomAccessFile("dados/futebol.db", "rw");

      if (arqExist == false || arq.length() == 0) {
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
    System.out.println();
    System.out.print("Insira o cnpj do clube: ");
    cnpjparaveri = entrada.nextLine();// AQUI PRECISA TRATAR O CPNJ;
    ft.setCnpj(cnpjparaveri);
    System.out.println();
    System.out.print("Insira a cidade do clube: ");
    ft.setCidade(entrada.nextLine());

    escreverArquivo(ft);

  }

  // -------------------Create - FIM---------------------------------//

  // ----------------------READ-------------------------//

  public boolean lerArquivoId(short idproc, fut ft2) throws Exception {
    RandomAccessFile arq;
    byte ba[];
    boolean idExist = false;
    String lapide;

    arq = new RandomAccessFile("dados/futebol.db", "rw");
    arq.seek(2);
    int tam = arq.readInt();
    long posicaosave = arq.getFilePointer();
    short idlido = arq.readShort();
    int contador = 0;
    boolean idencontrado = false;
    boolean idnaoexistente = false;

    long ultimaPosiArq = (long) arq.length();

    while (contador <= idproc && idencontrado == false && idnaoexistente == false) {

      if (idlido == idproc) {

        lapide = arq.readUTF();

        if ((lapide.equals(" ") == true)) {
          idencontrado = true;
          arq.seek(posicaosave);
        }

      }

      if ((idencontrado == false) && (posicaosave + tam < ultimaPosiArq)) {
        arq.seek(posicaosave);
        int converlt = (int) posicaosave;
        posicaosave = (long) tam + converlt;
        arq.seek(posicaosave);

        tam = arq.readInt();
        posicaosave = arq.getFilePointer();
        idlido = arq.readShort();

      } else {
        if (idencontrado == false) {
          idnaoexistente = true;
        }

      }

      contador++;

    }

    if (idencontrado == true && idlido == idproc) {
      ba = new byte[tam];
      arq.read(ba);
      ft2.fromByteArray(ba);
      // System.out.println(ft2.toString());
      idExist = true;

    }
    arq.close();

    return idExist;
  }

  public boolean lerArquivoNome(String nome, fut ft2) throws Exception {

    RandomAccessFile rafN;
    byte ba[];
    boolean nomeExist = false;
    long posiMudar = 0;
    long posiI = 0;
    long saveLapide = 0;
    String lapide;

    rafN = new RandomAccessFile("dados/futebol.db", "rw");
    long tamTotalArq = rafN.length();

    rafN.seek(2);
    int tamRegistro = rafN.readInt();
    posiI = rafN.getFilePointer();
    rafN.seek(rafN.getFilePointer() + 2);
    saveLapide = rafN.getFilePointer();
    rafN.seek(saveLapide + 3);
    String nomeR = rafN.readUTF();
    // rafN.seek(posiI);

    while (posiI + tamRegistro < tamTotalArq && (nome.equals(nomeR) == false)) {
      posiMudar = (long) tamRegistro;
      rafN.seek(posiMudar + posiI);
      tamRegistro = rafN.readInt();
      posiI = rafN.getFilePointer();
      rafN.seek(rafN.getFilePointer() + 2);
      saveLapide = rafN.getFilePointer();
      rafN.seek(saveLapide + 3);
      nomeR = rafN.readUTF();
      rafN.seek(posiI);

    }

    if (nome.equals(nomeR) == true) {

      rafN.seek(saveLapide);
      lapide = rafN.readUTF();
      if (lapide.equals(" ")) {
        rafN.seek(posiI);
        ba = new byte[tamRegistro];
        rafN.read(ba);
        ft2.fromByteArray(ba);
        nomeExist = true;
      }

    }

    rafN.close();

    return nomeExist;

  }

  public boolean procurarClube(String recebendo, fut ft2) {

    /*
     * como ta sendo feita a escrita
     * ID COMECO DO ARQUIVO + Tam do Arquiv +
     * ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)
     */
    // Escrita no Arquivo

    boolean idExist = false;

    try {

      // boolean existearq = verificarArquivo("dados/futebol.db");

      // if (existearq == true) {

      boolean idOrnot = recebendo.matches("-?\\d+");

      if (idOrnot == true) {

        // System.out.println("Qual ID você quer pesquisar no Arquivo ?");
        // short idlido = entradaLeituraId.nextShort();
        int idlidoum = Integer.parseInt(recebendo);
        short idlido = (short) idlidoum;
        idExist = lerArquivoId(idlido, ft2);

        if (idExist == false) {

          System.out.println("ID/Clube pesquisado não encontrado");
          System.out.println();

        }

      } else {
        idExist = lerArquivoNome(recebendo, ft2);

        if (idExist == false) {
          System.out.println("Nome do Clube pesquisado não encontrado");
          System.out.println();
        }

      }

      // } else {
      // System.out.println("ERRO: Arquivo não foi criado, não tem nada para ser
      // procurado !");
      // }

    } catch (Exception e) {
      String erro = e.getMessage();

      if (erro.contains("No such file or directory")) {

        System.out.println("Diretório do arquivo não encontrado !");
        return false;
      }
    }

    return idExist;
  }

  // ----------------------READ - FIM-------------------------//

  public void arquivoDelete(String id, Scanner verificarultimoDelete, fut ft2) {

    RandomAccessFile arq;

    boolean arquivoDeletado = false;
    try {
      arq = new RandomAccessFile("dados/futebol.db", "rw");

      boolean arqExist = procurarClube(id, ft2);

      if (arqExist == true) {

        int idproc = Integer.parseInt(id);
        String lapide;
        arq.seek(2);
        int tam = arq.readInt();
        long posicaosave = arq.getFilePointer();
        short idlido = arq.readShort();
        int contador = 0;
        boolean idencontrado = false;

        long ultimaPosiArq = (long) arq.length();

        while (contador <= idproc && idencontrado == false) {

          if (idlido == idproc) {

            lapide = arq.readUTF();

            if ((lapide.equals(" ") == true)) {
              idencontrado = true;
              arq.seek(posicaosave + 6);
            }

          }

          if ((idencontrado == false) && (posicaosave + tam < ultimaPosiArq)) {
            arq.seek(posicaosave);
            int converlt = (int) posicaosave;
            posicaosave = (long) tam + converlt;
            arq.seek(posicaosave);

            tam = arq.readInt();
            posicaosave = arq.getFilePointer();
            idlido = arq.readShort();

          }

          contador++;

        }

        if (idencontrado == true && idlido == idproc) {

          String idlido2 = String.valueOf(idlido);
          procurarClube(idlido2, ft2);
          System.out.println(ft2.toString());

          System.out.println("Você deseja deletar esse registro ?");
          verificarultimoDelete.reset();
          String ultVeri = verificarultimoDelete.nextLine();

          if ((ultVeri.toLowerCase().equals("sim") == true)) {
            arq.seek(posicaosave + 2);
            lapide = "*";
            System.out.println(arq.getFilePointer());
            arq.writeUTF(lapide);
            arquivoDeletado = true;
          } else {
            System.out.println("Registro não Deletado");
          }

        }

      } else {
        System.out.println("Registro não existente para ser deletado !");
      }

    } catch (Exception e) {
      System.out.println("Erro quando foi deletar um registro" + e.getMessage());
    }

    if (arquivoDeletado == true) {

      System.out.println("Registro Deletado com Sucesso");

    }

  }

  // ----------------------Delete - FINAL-------------------------//
}
