🧙‍♂️ Eldrin and the Magic Beams - Graph & Ray-Casting Optimization

Este repositório contém a solução otimizada em Java para o problema "Eldrin and the Magic Beams". O desafio consiste em ajudar o feiticeiro Eldrin a libertar feixes mágicos numa grelha de runas, garantindo que o corredor central é desobstruído sem causar colisões catastróficas.

Autores: Francisco Oliveira (67711) & Sérgio Garrido (67202) - P4

🚀 O Problema

Numa grelha de runas de tamanho máximo $200 \times 200$, existem até 10.000 feixes mágicos.
O objetivo é limpar um conjunto de colunas específicas (o "Corredor de Estabilização"). Os feixes movem-se apenas numa direção (Norte, Sul, Este ou Oeste) e não podem sobrepor-se.
Temos de determinar:

Se é possível libertar o corredor (Disaster caso exista um ciclo de dependências intransponível).

Se não for necessário mover feixes (False alarm).

A ordem exata em que os feixes devem ser libertados, priorizando sempre o feixe com o menor ID num cenário de empate.

🛠️ Arquitetura e Otimizações

A solução inicial lidava com a deteção de bloqueios comparando cada feixe com todos os outros, o que resultava numa complexidade de tempo de $O(B^2)$. Com $10.000$ feixes, isso gerava estrangulamentos de performance (Time Limit Exceeded).

O código foi refatorado e otimizado com as seguintes abordagens:

1. Grelha de Ocupação e Ray-Casting (Fim do O(B^2))

Em vez de comparações par-a-par, introduzimos uma matriz de ocupação (Occupancy Grid).

Mapeamos o estado inicial de todos os feixes numa matriz de 200 * 200.

Implementamos um algoritmo de Ray-Casting (traceBeamPath): cada feixe "dispara" um raio na sua direção de movimento para ver em que identificadores bate.

Ganho: A complexidade da construção do grafo passou de dependente do número de feixes ao quadrado, para uma operação leve dependente apenas do tamanho do tabuleiro.

2. Deteção Transitiva de Necessidade (Filtragem)

Eldrin apenas remove o que é estritamente necessário.

Usamos uma lógica de propagação baseada em grafos para detetar que feixes estão no corredor e quais os feixes que bloqueiam os feixes do corredor (direta ou indiretamente).

Isto evita processamento inútil de feixes que não interferem com a solução.

3. Ordenação Topológica Seletiva (Kahn's Algorithm)

A resolução da ordem de saída é feita com uma PriorityQueue (para garantir a regra do menor ID primeiro).

A fila é alimentada apenas com os feixes previamente marcados como "necessários".

Se no final o número de feixes processados for inferior ao total de feixes necessários, é detetado um ciclo fatal e o sistema retorna Disaster.

4. Estruturas de Dados Modernas e Fast I/O

Uso de Java Records (BeamCoordinates, BeamBoundary) para garantir a imutabilidade, baixo consumo de memória e código limpo.

Substituição de múltiplos System.out.print por um StringBuilder centralizado, evitando overhead massivo de Input/Output no terminal.
