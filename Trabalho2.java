import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Trabalho2 {
    static int VAZIO = -1;
    static int TAMANHO = 1000;
    static int QUANTIDADE = 700;
    static int acessos = 0;
    static int colisoes = 0;
    static int duplicadasDescartadas = 0;



// As quatro funções hash

    // Calcula o indice pelo resto da divisao da chave por 1000.
    // Retorna a posicao na tabela entre 0 e 999.
    static int hashDivisao(int chave) {
        return chave % 1000;
    }

    // Calcula o indice pelo resto da divisao da chave pelo numero primo 997.
    // Retorna a posicao na tabela entre 0 e 996.
    static int hashPrimo(int chave) {
        return chave % 997;
    }

    // Calcula o indice dividindo a chave em três partes e somando todas elas, extraindo o resto da divisão por 1000.
    // Retorna a posicao na tabela entre 0 e 999.
    static int hashDobra(int chave) {
        int parteFinal = chave % 1000;
        int parteMeio = (chave / 1000) % 1000;
        int parteInicio = chave / 1000000;
        return (parteFinal + parteMeio + parteInicio) % 1000;
    }

    // Calcula o quadrado da chave e divide por 100000, extraindo com o resto da divisão por 1000.
    // Retorna a posição com base no indíces da posição 6, 7 e 8 do quadrado da chave contando da direita para a esquerda com o valor inicial 1.
    static int hashMeioQuadrado(int chave) {
        long quadrado = (long) chave * chave;
        return (int) ((quadrado / 100000) % 1000);
    }



// Métodos

    // Verifica qual função hash deve ser utilizada para a respectiva função.
    // Retorna a posição calculada.
    static int aplicaHash(int funcao, int chave) {
        if (funcao == 1) {
            return hashDivisao(chave);
        } else if (funcao == 2) {
            return hashPrimo(chave);
        } else if (funcao == 3) {
            return hashDobra(chave);
        } else {
            return hashMeioQuadrado(chave);
        }
    }

    // Verifica o valor da função.
    // Retorna uma String identificando qual função hash foi utilizada.
    static String nomeFuncao(int funcao) {
        if (funcao == 1) {
            return "divisao";
        } else if (funcao == 2) {
            return "primo";
        } else if (funcao == 3) {
            return "dobra";
        } else {
            return "meioQuadrado";
        }
    }


    // Verifica o indetificador do conjunto.
    // Retorna a respectiva letra do conjunto.
    static String nomeConjunto(int conjunto) {
        if (conjunto == 1) {
            return "A";
        } else if (conjunto == 2) {
            return "B";
        } else if (conjunto == 3) {
            return "C";
        } else {
            return "D";
        }
    }

    // Recebe a posição como parâmetro somando + 1 e cálcula o resto pelo tamanho que é 1000.
    // Retorna uma nova posição.
    static int rehash(int posicao) {
        return (posicao + 1) % TAMANHO;
    }

    // Cria uma tabela e preenche todas as posições com -1 ou valor "VAZIO".
    // Retorna o vetor tabela criado.
    static int[] criaTabela() {
        int[] tabela = new int[TAMANHO];
        for (int indice = 0; indice < TAMANHO; indice++) {
            tabela[indice] = VAZIO;
        }
        return tabela;
    }

    // Insere uma chave na tabela utilizando rehashing linear para tratar de colisões.
    // Retorna 1 se inseriu com sucesso, 0 se a chave for duplicada, ou -1 se a tabela estiver cheia.
    static int insere(int[] tabela, int funcao, int chave) {
        int posicao = aplicaHash(funcao, chave);
        int passos = 0;
        while (passos < TAMANHO) {
            acessos++;
            passos++;
            if (tabela[posicao] == VAZIO) {
                tabela[posicao] = chave;
                return 1;
            }
            if (passos == 1) {
                colisoes++;
            }
            if (tabela[posicao] == chave) {
                return 0;
            }
            posicao = rehash(posicao);
        }
        return -1;
    }

    // Pesquisa uma chave na tabela utilizando rehashing linear.
    // Retorna o indíce onde a chave foi encontrada ou -1 se ela não existir.
    static int busca(int[] tabela, int funcao, int chave) {
        int posicao = aplicaHash(funcao, chave);
        int passos = 0;
        while (passos < TAMANHO) {
            acessos++;
            passos++;
            if (tabela[posicao] == VAZIO) {
                return -1;
            }
            if (tabela[posicao] == chave) {
                return posicao;
            }
            posicao = rehash(posicao);
        }
        return -1;
    }

    // Gera um conjunto de chaves sem valores repetidos.
    // Retorna o vetor contendo a quantidade de chaves solicitada.
    static int[] geraConjunto(Random sorteio, int conjunto, int quantidade) {
        int[] chaves = new int[quantidade];
        int gerados = 0;
        duplicadasDescartadas = 0;
        while (gerados < quantidade) {
            int candidata;
            if (conjunto == 1) {
                candidata = 1000000 + sorteio.nextInt(9000000);
            } else if (conjunto == 2) {
                candidata = (1000 + sorteio.nextInt(9000)) * 1000;
            } else if (conjunto == 3) {
                candidata = (100000 + sorteio.nextInt(900000)) * 10;
            } else {
                candidata = 1000001 + gerados;
            }
            boolean existe = false;
            for (int indice = 0; indice < gerados && !existe; indice++) {
                if (chaves[indice] == candidata) {
                    existe = true;
                }
            }
            if (existe == true) {
                duplicadasDescartadas++;
            } else {
                chaves[gerados] = candidata;
                gerados++;
            }
        }
        return chaves;
    }

    // COMPLETAR DEPOIS!!!!!!!!!!!!!!!!!!!! @Murilo-Caxambu pls
    // Não retorna nada, mas grava e imprime a linha do arquivo CSV.
    static void executaCombinacao(PrintWriter arquivo, int conjunto, int[] chaves, int funcao){
        int[] contagem = new int[TAMANHO];
        for(int indice = 0; indice < chaves.length; indice++){
            contagem[aplicaHash(funcao, chaves[indice])]++;
        }

        int usadas = 0;
        int maiorConcentracao = 0;
        int somaQuadrados = 0;
        for(int posicao = 0; posicao < contagem.length; posicao ++){
            if(contagem[posicao] > 0 ){
                usadas++;
            }
            if(contagem[posicao] > maiorConcentracao){
                maiorConcentracao = contagem[posicao];
            }
            somaQuadrados += contagem[posicao] * contagem[posicao];
        }
        int posicoesNuncaUsadas = TAMANHO - usadas;
        
        int[] tabela = criaTabela();
        acessos = 0;
        colisoes = 0;
        int maiorAcessos = 0;

        long inicio = System.nanoTime();
        for (int indice = 0; indice < chaves.length; indice++){
            int acessosAntes = acessos;
            insere(tabela, funcao, chaves[indice]);
            int acessosInsercao = acessos - acessosAntes;
            if (acessosInsercao > maiorAcessos){
                maiorAcessos = acessosInsercao;
            }
        }
        long fim = System.nanoTime();
        long tempoInsercao = (fim - inicio) / QUANTIDADE;
        int colisoesTotal = colisoes;
        int acessosInsercaoTotal = acessos;

        acessos = 0;
        for (int indice = 0; indice < chaves.length; indice++){
            busca(tabela, funcao, chaves[indice]);
        }
        int acessosBuscaTotal = acessos;
        float acessosBuscaSucessoMedia = (float) acessosBuscaTotal / QUANTIDADE;

        String linha = String.format("%s;%s;%d;%d;%d;%d;%d;%d;%d;%d;%.2f;%d", nomeConjunto(conjunto), nomeFuncao(funcao), QUANTIDADE, usadas, posicoesNuncaUsadas, maiorConcentracao, somaQuadrados, colisoesTotal, acessosInsercaoTotal, maiorAcessos, acessosBuscaSucessoMedia, tempoInsercao);
    
        System.out.println(linha);
        arquivo.println(linha);
    }

    // Realiza diversos testes com as funções anteriormente realizadas e verifica se o resultado sai como o esperado.
    // Exibe quantos testes foram aprovados.
    static void executaTestes(){
        int aprovados = 0;

        int[] executaChaves = {9837561, 1234567, 9999999, 1000000, 5654396, 4967000};
        int[] resultadoDivisao = {561, 567, 999, 0, 396, 0};
        int[] resultadoPrimo = {162, 281, 89, 9, 409, 943};
        int[] resultadoDobra = {407, 802, 7, 1, 55, 971};
        int[] resultadoQuadrado = {64, 556, 800, 0, 941, 890};
        for (int indice = 0; indice < 6; indice++){
            int chaveAtual = executaChaves[indice];
            if (
                hashDivisao(chaveAtual) == resultadoDivisao[indice] && 
                hashPrimo(chaveAtual) == resultadoPrimo[indice] && 
                hashDobra(chaveAtual) == resultadoDobra[indice] && 
                hashMeioQuadrado(chaveAtual) == resultadoQuadrado[indice]){
                    System.out.println("TESTE H" + (indice + 1) + ": APROVADO");
                    aprovados++;
            } else {
                System.out.println("TESTE H" + (indice + 1) + ": REPROVADO");
            }
        }

        Random sorteioH7 = new Random(1990);
        int[] chavesH7 = geraConjunto(sorteioH7, 1, 700);
        int posicoesInvalidas = 0;
        for (int indice = 0; indice < 700; indice++){
            for (int funcao = 1; funcao <= 4; funcao++){
                int posicaoObtida = aplicaHash(funcao, chavesH7[indice]);
                if (posicaoObtida < 0 || posicaoObtida > 999){
                    posicoesInvalidas++;
                }
            }
        }
        if (posicoesInvalidas == 0){
            System.out.println("TESTE H7: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE H7: REPROVADO");
        }


        int[] tabelaI = criaTabela();
        acessos = 0;
        colisoes = 0;

        insere(tabelaI, 1, 5001);
        insere(tabelaI, 1, 7001);
        insere(tabelaI, 1, 9001);
        if (tabelaI[1] == 5001 && tabelaI[2] == 7001 && tabelaI[3] == 9001){
            System.out.println("TESTE I1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE I1: REPROVADO");
        }

        if (colisoes == 2 && acessos == 6){
            System.out.println("TESTE I2: APROVADO");
            aprovados++;
        } else{
            System.out.println("TESTE I2: REPROVADO");
        }

        acessos = 0;
        if (busca(tabelaI, 1, 9001) == 3 && acessos == 3){
            System.out.println("TESTE I3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE I3: REPROVADO");
        }

        acessos = 0;
        if (busca(tabelaI, 1, 4001) == -1 && acessos == 4){
            System.out.println("TESTE I4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE I4: REPROVADO");
        }

        int[] tabelaI5 = criaTabela();
        insere(tabelaI5, 1, 1000999);
        insere(tabelaI5, 1, 2000999);
        if (tabelaI5[999] == 1000999 && tabelaI5[0] == 2000999){
            System.out.println("TESTE I5: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE I5: REPROVADO");
        }

        acessos = 0;
        int retornoInsercao = insere(tabelaI5, 1, 2000999);
        if (retornoInsercao == 0 && acessos == 2){
            System.out.println("TESTE I6: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE I6: REPROVADO");
        }


        Random sorteioS1 = new Random(1990);
        int[] chavesS1 = geraConjunto(sorteioS1, 1, 700);
        if (chavesS1[0] == 4429279 && chavesS1[1] == 3976163 && chavesS1[2] == 9187615 && chavesS1[699] == 9691227 && duplicadasDescartadas == 0) {
            System.out.println("TESTE S1: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE S1: REPROVADO");
        }

        Random sorteioS2 = new Random(1990);
        int[] chavesS2 = geraConjunto(sorteioS2, 2, 700);
        if (chavesS2[0] == 1279000 && chavesS2[1] == 7163000 && chavesS2[2] == 7615000 && chavesS2[699] == 7494000 && duplicadasDescartadas == 32) {
            System.out.println("TESTE S2: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE S2: REPROVADO");
        }

        Random sorteioS3 = new Random(1990);
        int[] chavesS3 = geraConjunto(sorteioS3, 3, 700);
        if (chavesS3[0] == 8292790 && chavesS3[1] == 3761630 && chavesS3[2] == 1876150 && chavesS3[699] == 6289620 && duplicadasDescartadas == 0) {
            System.out.println("TESTE S3: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE S3: REPROVADO");
        }

        Random sorteioS4 = new Random(1990);
        int[] chavesS4 = geraConjunto(sorteioS4, 4, 700);
        if (chavesS4[0] == 1000001 && chavesS4[1] == 1000002 && chavesS4[2] == 1000003 && chavesS4[699] == 1000700 && duplicadasDescartadas == 0) {
            System.out.println("TESTE S4: APROVADO");
            aprovados++;
        } else {
            System.out.println("TESTE S4: REPROVADO");
        }

        System.out.println("TESTES: " + aprovados + " de 17 aprovados");
    }

    // Executa os testes e os experimentos com os diferentes conjuntos de chaves e funções hash,
    // exibindo e salvando os resultados em um arquivo CSV.
    public static void main(String[] args) throws IOException{
        executaTestes();
        
        PrintWriter arquivo = new PrintWriter("resultados_trabalho2.csv");

        String cabecalho = "conjunto;funcao;chaves;posicoesUsadas;posicoesNuncaUsadas;maiorConcentracao;somaQuadrados;colisoes;acessosInsercaoTotal;maiorAcessosUmaInsercao;acessosBuscaSucessoMedia;tempoInsercaoNsPorOp";

        System.out.println(cabecalho);
        arquivo.println(cabecalho);

        for (int conjunto = 1; conjunto <= 4; conjunto++) {
        Random sorteio = new Random(1990);
        int[] chaves = geraConjunto(sorteio, conjunto, QUANTIDADE);

            for (int funcao = 1; funcao <= 4; funcao++) {
                executaCombinacao(arquivo, conjunto, chaves, funcao);
            }
        }
    }
}
