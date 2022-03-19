import java.util.*;

public class futebolprincipal {

  public static boolean realizarPartida(Scanner entrada) {

    fut time1 = new fut();
    fut time2 = new fut();
    arquivocrud arq = new arquivocrud();
    Boolean status = false;
    boolean status2 = false;
    entrada = new Scanner(System.in);
    System.out.print("Digite o nome do primeiro clube da partida: ");
    String timeUm = entrada.nextLine();
    System.out.println();
    System.out.print("Digite o nome do SEGUNDO clube da partida: ");
    String timeDois = entrada.nextLine();
    System.out.println();
    boolean retorno = true;

    long retornoTimeUm = arq.procurarClube(timeUm, time1);

    if (retornoTimeUm >= 0) {
      long retornoTimeDois = arq.procurarClube(timeDois, time2);

      if (retornoTimeDois >= 0) {

        short placarTimeUm = 0;
        short placarTimeDois = 0;
        byte pontos = 0;

        System.out.println("Digite o placar do Contronto entre o primeiro time e o segundo !");
        System.out.print("Gols Time " + timeUm + ": ");
        placarTimeUm = entrada.nextShort();
        System.out.println();
        System.out.print("Gols Time " + timeDois + ": ");
        placarTimeDois = entrada.nextShort();

        if (placarTimeUm > placarTimeDois) {

          pontos = 3;
          status = arq.arquivoUpdate(timeUm, entrada, "Parcial", pontos, time1);
          pontos = 0;
          status2 = arq.arquivoUpdate(timeDois, entrada, "Parcial", pontos, time2);

        } else {
          pontos = 3;
          if (placarTimeUm < placarTimeDois) {
            pontos = 3;
            status = arq.arquivoUpdate(timeDois, entrada, "Parcial", pontos, time2);
            pontos = 0;
            status2 = arq.arquivoUpdate(timeUm, entrada, "Parcial", pontos, time1);
          } else {
            pontos = 1;
            if (placarTimeUm == placarTimeDois) {
              status = arq.arquivoUpdate(timeUm, entrada, "Parcial", pontos, time1);
              status2 = arq.arquivoUpdate(timeDois, entrada, "Parcial", pontos, time2);
            }
          }

        }

      } else {
        System.out.println("Realização de partida CANCELADA, time 2 não encontrado");

        retorno = false;
      }

    } else {
      System.out.println("Realização de partida CANCELADA, time 1 não encontrado !");

      retorno = false;
    }

    if (status == true && status2 == true) {
      System.out.println("Realização de Partida Concluida !");

      System.out.println("------------------X------------------");
      System.out.println(time1.toString());
      System.out.println("------------------X------------------");
      System.out.println(time2.toString());
      System.out.println("------------------X------------------");

    } else {
      System.out.println("Partida NÃO realizada");
      retorno = false;
    }

    return retorno;
  }

  public static boolean eNumero(String str, String tipo) {

    boolean idOrnot = str.matches("-?\\d+");
    boolean verifica = false;
    if (idOrnot == true) {

      if (tipo.equals("Byte") == true) {

        int Inteiro = Integer.parseInt(str);

        if (Inteiro <= 127) {
          verifica = true;
        }

      }

    }

    return verifica;
  }

  public static void main(String[] args) {
    Scanner entrada = new Scanner(System.in);

    arquivocrud arqcru = new arquivocrud();
    fut futebas = new fut();

    String menuStr = "";
    byte menu = 0;
    byte zero = 0;
    int menuconvert = 0;
    boolean contador = false;
    boolean eNumero = false;

    while (contador == false) {
      System.out
          .println(
              "0 - Encerrar Programa \n1 - Cadastrar um Clube \n2 - Realizar partida\n3 - Ler um ID do arquivo\n4 - Realizar Atualização de um Registro\n5 - Realizar o Delete de um Registro ");

      menuStr = entrada.nextLine();
      eNumero = eNumero(menuStr, "Byte");

      if (eNumero == true) {
        menuconvert = Integer.parseInt(menuStr);
        menu = (byte) menuconvert;

        switch (menu) {
          case 0:
            contador = true;
            System.out.println("Encerrando o programa...");
            break;
          case 1:
            arqcru.criarClube(entrada);
            break;
          case 2:
            // System.out.println("CASE 2 - Realizar Partida");
            realizarPartida(entrada);
            break;
          case 3:

            System.out.println("Digite o ID ou Nome do Clube a ser procurado no arquivo");
            String entradaPesquisadeClube = entrada.nextLine();
            futebas.printarClubesExistentes(arqcru.procurarClube(entradaPesquisadeClube, futebas));
            break;
          case 4:
            System.out.println("Digite o ID ou Nome do Clube na qual será atualizado os dados !");

            String entradaUpg = entrada.nextLine();
            arqcru.arquivoUpdate(entradaUpg, entrada, "Completo", zero, null);

            break;
          case 5:

            System.out.println("Digite o ID para ser deletado");
            String idDelete = entrada.nextLine();
            arqcru.arquivoDelete(idDelete, entrada, futebas);
            break;

          case 9:
            arqcru.deletaTudo();
            break;

          default:
            System.out.println("Digito Não Valido... Inserir novamente o digito");

        }
        entrada = new Scanner(System.in);
      } else {
        System.out.println("Digito Não Valido... Inserir novamente o digito");

      }

    }

    entrada.close();

  }
}
