import java.io.PrintWriter;
import java.util.Random;

public class Trabalho2 {
    static int VAZIO = -1;
    static int TAMANHO = 1000;
    static int QUANTIDADE = 700;
    static int acessos = 0;
    static int colisoes = 0;
    static int duplicadasDescartadas = 0;

    //
    static int hashDivisao(int chave) {
        return chave % 1000;
    }

    static int hashPrimo(int chave) {
        return chave % 997;
    }

    static int hashDobra(int chave) {
        int parteFinal = chave % 1000;
        int parteMeio = (chave / 1000) % 1000;
        int parteInicio = chave / 1000000;
        return (parteFinal + parteMeio + parteInicio) % 1000;
    }

    static int hashMeioQuadrado(int chave) {
        long quadrado = (long) chave * chave;
        return (int) ((quadrado / 100000) % 1000);
    }

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

    static int rehash(int posicao) {
        return (posicao + 1) % TAMANHO;
    }

    static int[] criaTabela() {
        int[] tabela = new int[TAMANHO];
        for (int indice = 0; indice < TAMANHO; indice++) {
            tabela[indice] = VAZIO;
        }
        return tabela;
    }

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

    static void executaCombinacao(PrintWriter arquivo, int conjunto, int[] chaves, int funcao){
        int[] contagem = new int[TAMANHO];
        for(int indice = 0; indice < chaves.length; indice++){
            aplicaHash(funcao, chaves[indice]);
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
    }
}
