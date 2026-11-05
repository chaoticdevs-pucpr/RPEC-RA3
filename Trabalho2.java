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

    // COMPLETAR DEPOIS!!!!!!!!!!!!!!!!!!!!
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
            int AcessosInsercao = acessos - acessosAntes;
            if (AcessosInsercao > maiorAcessos){
                maiorAcessos = AcessosInsercao;
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

    //
    // ??????????????
    static void executaTestes(){

    }

    public static void main(String[] args) throws IOException{
        
    }
}
