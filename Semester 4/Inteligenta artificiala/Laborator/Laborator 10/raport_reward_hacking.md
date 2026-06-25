# Raport: Reward Hacking în Tetris (RL Framework)

**Obiectiv:** Observarea fenomenului de *reward hacking* prin modificarea funcției de recompensă a unui agent de tip DQN (`DQNAgent` din fișierul `advanced_rl_agents.py`).

## 1. Alegerea componentei și Predicția

**Componenta studiată:** Penalizarea pentru găuri (Hole Penalty).
În codul original, funcția de recompensă pedepsește construirea unor structuri neregulate prin formula:  
`reward -= holes_created * 5`.

**Intervenția:** Am ales să **inversez semnul** componentei (transformându-l în `reward += holes_created * 5`), recompensând astfel deliberat agentul pentru crearea unui număr cât mai mare de goluri sub piese, în loc să îl penalizez.

**Predicția comportamentului:**  
Dacă agentul primește puncte bonus pentru fiecare gaură nou creată, preconizez că se va declanșa instant un clasic *reward hacking*:
1. Agentul își va altera drastic politica, preferând să așeze piesele în cele mai ineficiente configurații posibile (ex. așezarea pieselor orizontale peste denivelări mari pentru a crea „tunele” sau „pivnițe” dedesubt).
2. Jocul se va scurta considerabil. Constatând că primește multe puncte pentru formarea găurilor, agentul va ignora cu desăvârșire obiectivul real (curățarea liniilor). Structura se va înălța rapid și jocul se va încheia prin *Game Over* prematur.
3. Media liniilor curățate va tinde spre zero, dar cumulul de reward pe parcursul primelor mutări până la pierderea jocului ar putea fi, din perspectiva matematică a agentului, unul „optim”.

---

## 2. Rezultatul Empiric (Testare)

Pentru test s-a folosit scriptul de antrenament setat pe o durată scurtă (30 de episoade), simulând atât scenariul de bază, cât și scenariul modificat.

**DQN Standard (`reward -= holes_created * 5`)**:
- **Durata jocurilor (survival):** Relativ normală. Agentul reușește să amâne *Game Over*-ul, încercând activ să evite așezările haotice datorită presiunii penalizărilor de găuri și înălțime.
- **Linii curățate:** Agentul formează reflexe fundamentale de a ține board-ul ordonat, reușind să finalizeze linii.

**Hole-Loving DQN (`reward += holes_created * 5`)**:
- **Număr mediu de linii curățate:** **0.00**.
- **Durata jocurilor:** A devenit **considerabil mai scurtă**. Agentul a căutat intenționat plasări pe coloane înguste doar pentru a umbri restul coloanelor și a maximiza contorul de „holes”. A urcat turnul din piese în mod vertical până sus aproape instantaneu, pierzând fiecare episod în timp record, dar colectând un bonus mare per mutare înainte să piardă.

---

## 3. Explicația Conceptuală (Analiză Post-Hoc)

Comportamentul afișat exemplifică perfect conceptul de **aliniere greșită a obiectivelor (misspecified objective)**. 

Agentul de Reinforcement Learning nu are nicio înțelegere intrinsecă a regulilor sau scopului uman dintr-un joc de „Tetris”. El este strict un algoritm de optimizare matematică a unei funcții de recompensă cumulative ($\max \sum \gamma^t R_t$). 

Când funcția de recompensă oferea `+5` puncte pentru fiecare gaură adăugată:
1. **Scurtătura (The Hack):** Agentul a descoperit că efortul și numărul de mutări necesare pentru a obține recompense din *găuri* e mult mai ieftin și mai facil decât să coordoneze zeci de mutări precise pentru a obține `+10` din crearea unei linii.  
2. **Ignorarea finalității:** De ce preferă jocul scurt deși există o penalizare imensă de `-100` pentru *Game Over*? Deoarece acumularea repetată de `+5` (multiplicată de fiecare pătrățel de gaură obținut din plasarea intenționat greșită a pieselor) compensează rapid acel minus de final. Agentul ajunge să „considere” că merită să moară repede, atât timp cât strânge zeci de puncte bonus pe parcursul celor câteva mutări jucate.

**Concluzie:** 
Experimentul demonstrează cum un **reward shaping imprudent** (sau o singură componentă dezechilibrată) poate ruina complet intenția de design. Algoritmul va găsi întotdeauna calea minimului efort matematic pentru a exploata funcția de recompensă, ignorând funcționalitatea vizată per ansamblu.