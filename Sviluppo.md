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
  * Vedere se ci sono pedine dello stesso colore in fila:
    * Vedere se ci sono pedine in fila in verticale
    * Se in orizzonatale
    * Se in obliguo
  * Contare quante pedine in fila ci sono
  * controllare se sono piu di 5 in fila
* gestorePareggio 
* handelr partita (forse)
    * gestire i turni
    * gestire lo stato della partita (in corso, vinta, pareggio)
    * Salvare una partita
    * Caricare una partita

## TEST
* ~~(3,3) -> incrocio della riga 3 con la colonna 3~~
* ~~(13,1) -> incrocio della riga 13 con la colonna 1~~
* ~~(11,15) -> incrocio della riga 11 con la colonna 15~~
* “vuoto” -> incrocio (5,5) senza alcuna pedina
* “vuoto” -> incrocio (11,15) senza alcuna pedina
* “Occupato” -> incrocio (7,7) senza alcuna pedina
* “bianco” -> incrocio (3,1) senza alcuna pedina
* “bianco” -> incrocio (11,11) senza alcuna pedina
* “Bianco” -> incrocio (1,15) senza alcuna pedina
* “Nero” -> incrocio (1,15) senza alcuna pedina
* “Nero” -> incrocio (9,7) senza alcuna pedina
* “Nero” -> incrocio (11,9) senza alcuna pedina
* “” -> ha pedine del suo colore adiacenti?
* Sapere se c’è una serie di adiacenze/PedineInFila
* Aggiornare una PedineInFila
* Sapere quanto lunga è una fila
* Sapere se si puo ancora vincere


CLASSI
* ~~Tavola da gioco~~
* Player
* Partita
* Handlervittoria (controlla se c’è vittoria)
* GestoreTavola (gestisce la tavola da gioco)
* GestorePlayer (gestisce i player)
* GestorePartita (gestisce la partita)
* GestoreMosse (gestisce le mosse)
* Adiacenza (pedine in fila: estremi della fila )
* CheckVittoria(controlla guardando gli estremi che non si superi la lunghezza 5)
 
CodiceIdentificativo  stato (vuoto, bianco, nero)

#PLAYER


Top-down design
* Piazzare un pezzo
    * Controllare se l’incrocio è libero