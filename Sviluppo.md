# GOMOKU

## REGOLE
Tavola 15x15\
Regole vittoria -> 5 precisi in fila o più\
Si gioca a turno\
2 player\
Inizia il nero


## FEATURES
* ~~Vedere se un incrocio è occupato~~
* ~~Vedere il colore della pedina se presente~~
  ~~* Possibile refactoring con funzione "stato" che ti dice se l’incriocio è libero, bianco o nero~~
~~* Piazzare una pedina~~


## FEATURE HANDLER
* GestoreTavola
  * controllare se si puo piazzare una pedina(se è occupato o fuori dalla griglia)
  * Funzione piazzaPedina
+ GestoreVittora
  * controllare la vittoria
  ~~* Vedere se ci sono pedine dello stesso colore in fila~~
  ~~* Contare quante pedine in fila ci sono~~
  ~~* controllare se sono piu di 5 in fila~~
* gestorePareggio 
* handelr partita (forse)(è la partita stessa il gestore finale)
    * gestire i turni
    * gestire lo stato della partita (in corso, vinta, pareggio)
    * Salvare una partita
    * Caricare una partita



CLASSI
* ~~Tavola da gioco~~
~~* WinCheker (controlla se c’è vittoria)~~
* MoveService (gestisce e convalida le mosse)
* DrawChecker (gestisce il pareggio)
  * si puo creare una funzione tipo canWin(black) e si fa un && per controllare se nessunio dei due puo vincere
* Player
* Game
  * gestisce lo  stato della partita(IN_PROGRESS, WIN_BLACK, WIN_WHITE, DRAW)(loop infinito finchè non c’è vittoria o pareggio)
  * gestisce i turni(a chi tocca ed a quale turno siamo arrivati)


TO DO LIST
* refactoring test GridTest(più compatti)
* refactoring test WinCheckerTest(più compatti ed rifare le firme per elimanare il metodo duplicato isWinningMove)
* rinominare Cross in CrossState? o qualcosa del genere tipo
* eliminare da Grid le funzioni ripetute mantenendo solo quelle con point nella firma

TO DO?\ DA VALUTARE
* un lineScanner da usare nei checker per scanzionare le varie direzioni ed eliminare ripetizioni