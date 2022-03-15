import java.io.RandomAccessFile;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class tp01 {

  // -------------------FUNCAO-PARA-TESTAR-O-ARQUIVO----------------//

  public static void deletaTudo() throws IOException {

    PrintWriter writer = new PrintWriter("dados/futebol.db");
    writer.print("");
    writer.close();

  }

  public static boolean verificaCPNJ(String cnpj) {

    boolean verificador = false;
    int tamcnpj = cnpj.length();

    if (tamcnpj == 18) {

      for (int i = 0; i < tamcnpj; i++) {

        char a = cnpj.charAt(i);

        if ((i >= 0 && i <= 1) || (i >= 3 && i <= 5) || (i >= 7 && i <= 9) || (i >= 11 && i <= 14)
            || (i >= 16 && i <= 17)) {

          if (!(a >= 48 && a <= 57)) {
            verificador = false;
            return verificador;
          }

        }

        if (i == 2) {

          if (!(a == 46)) {
            verificador = false;
            return verificador;
          }

        }

        if (i == 6) {
          if (!(a == 46)) {
            verificador = false;
            return verificador;
          }
        }

        if (i == 10) {
          if (!(a == 47)) {
            verificador = false;
            return verificador;
          }
        }

        if (i == 15) {
          if (!(a == 45)) {
            verificador = false;
            return verificador;
          }
        }
      }
      verificador = true;
    }

    return verificador;

  }

  public static boolean verificarArquivo(String caminho) {

    File f = new File(caminho);

    boolean arquivoexiste = false;

    if (f.exists()) {
      if (f.isFile()) {
        arquivoexiste = true;
      }
    }

    return arquivoexiste;
  }

  // -------------------Create---------------------------------//

  public static void escreverArquivo(fut ft) {

    /*
     * como ta sendo feita a escrita
     * ID COMECO DO ARQUIVO + Tam do Arquiv +
     * ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)
     */
    // Escrita no Arquivo
    RandomAccessFile arq;
    byte[] ba;

    try {

      boolean arqExist = verificarArquivo("dados/futebol.db");
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
      System.out.println("ERRO NA ESCRITA !");
    }

    System.out.println("------X------");
    System.out.println(ft.toString());

  }

  public static void criarClube(Scanner entradaclube) {

    fut ft = new fut();

    String cnpjparaveri = null;

    System.out.print("Escreva o nome do clube: ");
    ft.setNome(entradaclube.nextLine());
    System.out.println();
    System.out.print("Insira o cnpj do clube: ");
    cnpjparaveri = entradaclube.nextLine();// AQUI PRECISA TRATAR O CPNJ;
    System.out.println();
    boolean veri = verificaCPNJ(cnpjparaveri);
    if (veri == true) {
      ft.setCnpj(cnpjparaveri);
    } else {
      System.out.println("CNPJ COM ERRO AO SER INSERIDO ! ");
      System.out.println();
    }
    System.out.print("Insira a cidade do clube: ");
    ft.setCidade(entradaclube.nextLine());

    escreverArquivo(ft);
  }

  // ----------------------READ-------------------------//

  public static boolean lerArquivoId(short idproc, fut ft2) {
    RandomAccessFile arq;
    byte ba[];
    boolean idExist = false;
    String lapide;

    try {

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

    } catch (Exception e) {
      System.out.println("Erro na leitura 2 de arquivo !");
    }

    return idExist;
  }

  public static boolean lerArquivoNome(String nome, fut ft2) {

    RandomAccessFile rafN;
    byte ba[];
    boolean nomeExist = false;
    long posiMudar = 0;
    long posiI = 0;
    long saveLapide = 0;
    String lapide;

    try {
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

      while (posiI + posiMudar < tamTotalArq && (nome.equals(nomeR) == false)) {
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

    } catch (Exception e) {
      System.out.println("ERRO: NO READ do arquivo a procura por NOME !");
    }

    return nomeExist;

  }

  public static boolean procurarClube(String recebendo, fut ft2) throws IOException {

    /*
     * como ta sendo feita a escrita
     * ID COMECO DO ARQUIVO + Tam do Arquiv +
     * ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)
     */
    // Escrita no Arquivo

    boolean existearq = verificarArquivo("dados/futebol.db");
    boolean idExist = false;

    if (existearq == true) {

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

    } else {
      System.out.println("ERRO: Arquivo não foi criado, não tem nada para ser procurado !");
    }

    return idExist;
  }
  // ----------------------Delete-------------------------//

  public static void arquivoDelete(String id, Scanner verificarultimoDelete, fut ft2) {

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

  // ----------------------Delete-------------------------//
  public static void main(String[] args) throws IOException {
    Scanner entrada = new Scanner(System.in);
    Scanner entradaclube = new Scanner(System.in);
    Scanner entradaPesquisa = new Scanner(System.in);
    Scanner entradaDelete = new Scanner(System.in);
    Scanner verificarultimoDelete = new Scanner(System.in);

    fut ft2 = new fut();
    byte menu = 0;
    boolean contador = false;
    while (contador == false) {

      System.out
          .println(
              "0 - Encerrar Programa \n1 - Cadastrar um Clube \n2 - Realizar partida\n3 - Ler um ID do arquivo\n4 - Realizar Atualização de um Registro\n5 - Realizar o Delete de um Registro ");

      menu = entrada.nextByte();

      switch (menu) {
        case 0:
          contador = true;
          System.out.println("ENCERRANDO O PROGRAMA...");
          break;
        case 1:
          criarClube(entradaclube);
          break;
        case 2:
          System.out.println("CASE 2 - Realizar Partida");
          break;

        case 3:
          System.out.println("Digite o ID ou Nome do Clube a ser procurado no arquivo");
          String entradaClube = entradaPesquisa.nextLine();
          Boolean receberClube = procurarClube(entradaClube, ft2);

          if (receberClube == true) {
            System.out.println(ft2.toString());
          }

          break;

        case 4:
          System.out.println("UPDATE");
          break;
        case 5:
          System.out.println("Digite o ID para ser deletado");
          String idDelete = entradaDelete.nextLine();
          arquivoDelete(idDelete, verificarultimoDelete, ft2);
          break;

        case 9:
          deletaTudo();// apagar isso quando entregar junto com a excessao no main
          break;
        default:
          System.out.println("Opção não contrada !");
          break;
      }
    }
    entrada.close();
    entradaclube.close();
    entradaPesquisa.close();
    entradaDelete.close();
    verificarultimoDelete.close();

  }
}
