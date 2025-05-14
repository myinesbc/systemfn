import java.util.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
      
      Provas provas = new Provas();
      System.out.println("Digite a nota da A1");
      provas.setA1(scanner.nextDouble());
      System.out.println("Digite a nota da A2");
      provas.setA2(scanner.nextDouble());
      System.out.println("Digite a nota da A3");
      provas.setA3(scanner.nextDouble());
      
      NotaTotal total = new NotaTotal();
      double soma = provas.getA1() + provas.getA2() + provas.getA3();
      total.setNotaTotal(soma);
      
      Condicao status = new Condicao();
      if(total.getNotaTotal() < 70){
      status.setCondicao("Reprovado!");
      }else{
      status.setCondicao("Aprovado!");
      }
      
      System.out.println("Nota da A1 : " + provas.getA1());
      System.out.println("Nota da A2 : " + provas.getA2());
      System.out.println("Nota da A3 : " + provas.getA3());
      System.out.println("Nota Total : " + total.getNotaTotal());
      System.out.println("O Aluno está : " + status.getCondicao());
  }
}
