import java.util.*;

public class futebolprincipal {

  public static boolean realizarPartida(Scanner entrada, Scanner entradaUpdate) {

    fut time1 = new fut();
    fut time2 = new fut();
    arquivocrud arq = new arquivocrud();
    Boolean status = false;
    boolean status2 = false;

    System.out.print("Digite o nome do primeiro clube da partida: ");
    String timeUm = entrada.nextLine();
    System.out.println();
    System.out.print("Digite o nome do SEGUNDO clube da partida: ");
    String timeDois = entrada.nextLine();
    System.out.println();

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

          status = arq.arquivoUpdate(timeUm, entradaUpdate, "Parcial", pontos, time1);
          pontos = 0;
          status2 = arq.arquivoUpdate(timeDois, entradaUpdate, "Parcial", pontos, time2);

        } else {
          pontos = 3;
          if (placarTimeUm < placarTimeDois) {
            pontos = 3;
            status = arq.arquivoUpdate(timeDois, entradaUpdate, "Parcial", pontos, time2);
            pontos = 0;
            status2 = arq.arquivoUpdate(timeUm, entradaUpdate, "Parcial", pontos, time1);
          } else {
            pontos = 1;
            if (placarTimeUm == placarTimeDois) {
              status = arq.arquivoUpdate(timeUm, entradaUpdate, "Parcial", pontos, time1);
              status2 = arq.arquivoUpdate(timeDois, entradaUpdate, "Parcial", pontos, time2);
            }
          }
        }

      } else {
        System.out.println("Realização de partida CANCELADA, time 2 não encontrado");
        return false;
      }

    } else {
      System.out.println("Realização de partida CANCELADA, time 1 não encontrado !");
      return false;
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
      return false;
    }

    return true;
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
    Scanner entradaclube = new Scanner(System.in);
    Scanner entradaPesquisa = new Scanner(System.in);
    Scanner vericarUDelete = new Scanner(System.in);
    Scanner entradaUpdate = new Scanner(System.in);
    Scanner entradaRealizarpartida = new Scanner(System.in);
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
            arqcru.criarClube(entradaclube);
            break;
          case 2:
            // System.out.println("CASE 2 - Realizar Partida");
            realizarPartida(entradaRealizarpartida, entradaUpdate);
            break;
          case 3:
            System.out.println("Digite o ID ou Nome do Clube a ser procurado no arquivo");
            String entradaPesquisadeClube = entradaPesquisa.nextLine();
            futebas.printarClubesExistentes(arqcru.procurarClube(entradaPesquisadeClube, futebas));
            break;
          case 4:
            System.out.println("Digite o ID ou Nome do Clube na qual será atualizado os dados !");
            entradaUpdate = new Scanner(System.in);
            String entradaUpg = entradaUpdate.nextLine();
            arqcru.arquivoUpdate(entradaUpg, entradaUpdate, "Completo", zero, null);

            break;
          case 5:
            System.out.println("Digite o ID para ser deletado");
            String idDelete = vericarUDelete.nextLine();
            arqcru.arquivoDelete(idDelete, vericarUDelete, futebas);
            break;

          case 9:
            arqcru.deletaTudo();
            break;

          default:
            System.out.println("Digito Não Valido... Inserir novamente o digito");

        }
      } else {
        System.out.println("Digito Não Valido... Inserir novamente o digito");

      }

    }

    entrada.close();
    entradaPesquisa.close();
    entradaclube.close();
    vericarUDelete.close();
    entradaUpdate.close();
    entradaRealizarpartida.close();

  }
}
