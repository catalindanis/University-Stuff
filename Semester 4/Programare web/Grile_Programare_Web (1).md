# Grile Programare Web - Banca de intrebari (unice)

## CSS

**1. Care dintre urmatoarele sunt prefixe acceptate pe unele browsere pentru proprietatile CSS experimentale?**
- -webkit-
- -o-
- -moz-
- -safari-
- -chrome-

**2. Pentru a centra un element cu display-ul block in cadrul unui alt container se foloseste**
- align: center
- margin: 0 auto
- align: middle
- text-align: center

**3. Care dintre sintaxele CSS de mai jos este corecta/sunt corecte?**
- body:color=red
- {body: color=red}
- body{color:#abc}
- {body; color:blue}
- body{color:yellow}

**4. Care dintre urmatoarele elemente au toate display-ul implicit block?**
- div, p, ul, ol, h1
- ul, ol, p, table, input
- ul, ol, p, img, div
- input, div, p, form

**5. Care dintre urmatoarele elemente au display-ul implicit inline?**
- a, span, b, i, li
- img, span, b, i, a
- p, a, b, i, span
- b, i, li, span, img

**6. Care dintre urmatoarele afirmatii sunt adevarate:**
- definitiile de stil interne au prioritate inaintea celor externe
- o definitie de stil aplicata clasei "bate" ca prioritate definitia de stil aplicata pe "tag"
- o proprietate din cadrul unui stil inline suprascrie o proprietate dintr-o definitie de stil aplicata elementului unui selector bazat pe id

**7. Dandu-se doua containere, unul parinte si unul fiu, care dintre urmatoarele pozitionarii are/au sens:**
- pozitionarea unui fiu "absolute" in cadrul unui parinte cu pozitionare "relative"
- pozitionarea unui fiu "relative" in cadrul unui parinte cu pozitionare "virtual"
- pozitionarea unui fiu "flex" in cadrul unui parinte cu pozitionare "relative"
- pozitionarea unui fiu "fixed" in cadrul unui parinte cu pozitionare "relative"

**8. Pozitionarea relative este folosita pentru:**
- a pozitiona/muta un element in diferite directii relativ la pozitia sa normala
- a pozitiona un element relativ la obiectul document
- a pozitiona un element relativ la fereastra browserului (obiectul window)
- un container parinte care are elemente fiu pozitionate absolut

**9. Care dintre urmatoarele afirmatii referitoare la filtrele de vizibilitate sunt adevarate?**
- Selectorul :hidden se foloseste pentru a selecta elementele care ocupa spatiu in pagina
- Selectorul :visible se foloseste pentru a selecta elementele care au visibility:hidden si opacity:0
- Selectorul :visible se foloseste pentru a selecta toate elementele care ocupa spatiu in pagina
- Selectorul :hidden se foloseste pentru a selecta toate elementele ascunse in pagina
- Selectorul :visible se foloseste pentru a selecta elementele care au display:none sau width/height:0

**10. Pentru a defini o lista ordonata a caror itemi sunt precedati de litere din alfabetul grec in CSS se foloseste:**
- list-style-type: lower-greek
- list-type: lower-greek
- list: lower-greek

**11. #RRAABB este un cod de culoare valid?**
- Nu, deoarece codul de culoare hexazecimal trebuie sa contina inca doua cifre pentru opacitate
- Da, deoarece valorile sunt specificate in baza 16
- Da, deoarece RR specifica cantitatea de rosu, AA cantitatea de verde si BB cantitatea de albastru
- Nu, deoarece valorile nu sunt specificate in baza 16

**12. Fie urmatoarea secventa de cod HTML/CSS. Pe ce fundal va fi afisat textul "Cocosul canta"?**

```css
<style type="text/css">
    #id2 {
        background-color: blue;
    }

    div #id1 .class2 {
        background-color: green;
    }
    div .class1 #id2 {
        background-color: yellow;
    }

    #id1 {
        background-color: red;
    }
</style>
<div>
    <div class="class1" id="id1">
        <div class="class2" id="id2">
            Cocosul canta
        </div>
    </div>
</div>
```

- Rosu
- Verde
- Galben
- Albastru

**13. Considerand urmatoarele elemente html:**

```html
<div class="info">John Doe</div>

<div class="info number" id="age">25</div>
```

**Ce selector CSS ar selecta doar cel de-al doilea div (cel cu continutul "25")?**
- .info.number
- .info .number
- .info #age
- .info[id]


## HTML

**14. Pentru inserarea de diacritice intr-un document HTML se pot folosi**
- Entitati HTML
- Editoare capabile sa salveze fisierul UTF-8 si specificarea acestui set de caractere in sectiunea head a documentului HTML
- Tastatura setata pe limba romana si specificarea atributului lang="ro-RO" la tagul HTML

**15. Care este forma corecta de folosire a tag-ului img in cadrul unui document XHTML:**
- &lt;img&gt; poza.jpg &lt;/img&gt;
- &lt;img src="poza.jpg"/&gt;
- &lt;img src="poza.jpg"&gt;&lt;/img&gt;
- &lt;img src="poza.jpg"&gt;

**16. Care dintre urmatoarele tag-uri HTML sunt deprecated in HTML5:**
- b
- body
- font
- center

**17. Cum se insereaza corect un fisier extern JavaScript denumit test.js intr-un document HTML?**
- &lt;script src="test.js"&gt;&lt;/script&gt;

**18. Cum se insereaza corect un fisier extern JavaScript denumit test.js intr-un document HTML? (varianta cu mai multe optiuni)**
- &lt;script src="test.js"&gt;&lt;/script&gt;
- &lt;script href="test.js"&gt;
- &lt;script src="test.js"&gt;
- &lt;script name="test.js"&gt;&lt;/script&gt;

**19. Care dintre urmatoarele categorii de aplicatii se pot folosi de DOCTYPE pentru a "intelege" mai bine documentul HTML**
- Serverele web
- Editoarele si medii IDE (Integrated Development Environment)
- Motoarele de cautare
- Browserele

**20. Care dintre urmatoarele tag-uri este folosit pentru definirea de stiluri CSS interne?**
- style
- css
- class
- script

**21. Care dintre urmatoarele informatii despre un URL accesat prin POST sunt adevarate:**
- Reacesarea URL-ului respectiv prin reload (refresh) la pagina presupune o confirmare din partea utilizatorului
- URL-ului respectiv i se poate face bookmark (poate fi adaugat la favorite)
- URL respectiv poate fi partajat (share) pe Facebook, Whatsapp, Skype, etc.

**22. Care dintre urmatoarele informatii despre tagul &lt;title&gt; sunt adevarate?**
- este folosit in mare masura de catre motoarele de cautare
- este optional
- este afisat in bara de titlu sau de bookmarks (favorite)
- nu exista tag-ul title, ci doar atributul title

**23. Elementul &lt;th&gt; trebuie folosit in cadrul unui tabel pentru:**
- centrarea textului si bold-area continutului unei celule
- definirea unei celule avand semnificatia de celula ce face parte din header-ul tabelului
- definirea unui nou rand in tabel

**24. Care dintre informatiile de mai jos despre atributele readonly si disabled sunt adevarate?**
- valoarea unui input readonly se trimite serverului la submit, pe cand cea a unui input disabled nu se trimite serverului
- valoarea unui input readonly poate fi modificata din JavaScript pe cand cea a unui input disabled nu poate fi editata nici din JavaScript
- atributele specifica acelasi lucru, faptul ca un input readonly sau disabled nu poate fi editat

**25. Care dintre urmatoarele antete sunt obligatorii pentru o cerere HTTP in cazul folosirii protocolului HTTP/1.1:**
- Content-Type
- Host
- User-Agent
- Cookie

**26. Un formular care contine un input de tip file trebuie:**
- submis prin metoda GET
- submis prin metoda POST
- sa aiba specificat atributul accept care specifica tipul fisierelor ce se pot trimite la server
- sa aiba specificat atributul enctype setat la multipart/form-data
- sa aiba specificat atributul content care sa indice spre continutul fisierului

**27. Care dintre urmatoarele afirmatii despre metoda HTTP POST sunt adevarate:**
- trebuie neaparat folosita pentru a submite un formular care contine un input de tip video
- este recomandat a fi folosita pentru a submite un formular care contine un input de tip password
- trebuie neaparat folosita pentru a submite un formular care contine un input de tip file

**28. Care sunt asemanarile si diferentele dintre atributele id si name?**
- orice tag accepta atributul id, nu toate tagurile accepta atributul name
- doua elemente accepta atributul name, nu toate tagurile accepta atributul id
- atributul name se foloseste pentru referirea unui element din CSS/JavaScript ([name="value"] {color: red;})
- doua elemente HTML pot avea acelasi nume, dar nu pot avea acelasi id
- atributul id se foloseste pentru referirea unui element in CSS/JavaScript


## JavaScript

**29. Care dintre următoarele cuvinte/caractere nu sunt rezervate în JavaScript și se pot folosi ca și nume de variabile?**
- this, a, b
- with, a, b
- $, a, b
- let, a, b

**30. Ce se intelege prin scop global in JavaScript?**
- obiectul window
- obiectul document
- variabila this
- scopul imediat exterior celui in care este declarata o functie

**31. Cum se declara corect un array in JavaScript?**
- var studenti = ["Mihai", "Cristina", "Paula", "Dan"]
- var studenti = ("Mihai", "Cristina", "Paula", "Dan")
- var studenti = "Mihai", "Cristina", "Paula", "Dan"
- var studenti = {"Mihai", "Cristina", "Paula", "Dan"}

**32. Cum se afiseaza un mesaj prin intermediul unei ferestre de dialog modale in JavaScript?**
- modalMessage("Mesaj")
- alert("Mesaj")
- modalBox("Mesaj")
- alertBox("Mesaj")

**33. O variabila x ce contine valoarea numerica 7 poate fi declarata in JavaScript astfel:**
- var x = 7
- var x:= 7
- x = new Number(7)
- let x = 7
- x = 7

**34. In JavaScript typeof 1/0 este NaN pentru ca:**
- 1/0 este NaN si typeof NaN este NaN
- 1/0 este egal cu Infinity si typeof Infinity este NaN
- type of 1/0 nu are rezultatul NaN, ci Infinity
- type of 1 este Number si Number nu poate imparti la 0, rezultatul fiind NaN

**35. Unde se poate insera cod JavaScript in cadrul unui document HTML?**
- atat in sectiunea &lt;head&gt; cat si &lt;body&gt;
- doar in sectiunea &lt;head&gt;
- doar in sectiunea &lt;body&gt;

**36. În care element HTML se plaseaza codul JavaScript?**
- &lt;javascript&gt;
- &lt;script&gt;
- &lt;js&gt;

**37. Ce face urmatoarea secventa de cod:**

```javascript
$('li:first').addClasss('patrat').addClass('deplasat').addClass('colorat');
```

- selecteaza toate listele si le adauga clasele patrat, deplasat, colorat
- selecteaza primul element din fiecare lista si ii adauga clasele patrat, deplasat, colorat
- selecteaza toate elementele din liste si le adauga clasele patrat, deplasat, colorat
- selecteaza primul li si ii adauga clasele patrat, deplasat, colorat

**38. Ce face urmatoarea secventa de cod $(':not(p)').addClass('patrat').addClass('colorat');**
- selecteaza toate elementele cu exceptia paragrafelor si le adauga clase patrat si colorat
- selecteaza toate elementele care au clasa patrat si clasa colorat cu exceptia paragrafelor
- selecteaza toate elementele cu exceptia paragrafelor care au clasa patrat si clasa colorat
- selecteaza toate paragrafele care au clasa patrat si colorat

**39. Ce face urmatoarea secventa de cod:**

```javascript
$('ul:has(li)').addClass('patrat').addClass('deplasat').addClass('colorat');
```

- selecteaza toate elementele din liste si le adauga clasele patrat, deplasat, colorat
- selecteaza listele neordonate care au cel putin un element si le adauga clasele patrat, deplasat, colorat
- selecteaza toate listele care au cel putin un element si le adauga clasele patrat, deplasat, colorat
- selecteaza listele ordonate care au cel putin un element si le adauga clasele patrat, deplasat, colorat
- selecteaza toate listele si le adauga clasele patrat, deplasat, colorat

**40. Ce face urmatoarea secventa de cod**

```javascript
var content = $('li').html();
$('li').append('<em> ' + content + '</em>');
```

- introduce continutului primului list item dupa fiecare list item
- introduce content dupa primul list item
- introduce continutului primului list item inaintea fiecarui list item
- continutul din primul li va fi scris cu italic
- continutul din toate li-urile vor fi scrise cu italic

**41. Ce se va afisa in consola browserului dupa executarea urmatorului cod Javascript?**

```javascript
if (1 === '1') {
    console.log("1 === '1'");
} else if (1 == true) {
    console.log("1 == true");
} else if (1 === 1.0) {
    console.log("1 === 1.0");
}
```

- nimic
- 1 === 1.0
- 1 == true
- 1 === '1'


## jQuery

**42. Care dintre urmatoarele sunt scriptlet-uri valide in PHP?**
- &lt;% %&gt;
- &lt;?php&gt;&lt;/?&gt;
- &lt;?= ?&gt;
- &lt;? ?&gt;, cu conditia setarii unei optiuni de configurare in php.ini
- &lt;?php ?&gt;
- &lt;php&gt;&lt;/php&gt;

**43. Care dintre urmatoarele metode jQuery folosesc AJAX:**
- $.ajax, $.get, load
- $.post, $.request, load
- $.ajax, $.get, $.post, $.request
- $.get, load, unload

**44. Aranjati in ordine, de la cea mai simpla si putin customizabila, la cea mai configurabila si parametrizabila urmatoarele functii care permit realizarea unui apel AJAX din jQuery:**
- $.get, load, $.post
- $.ajax, load, $.get
- load, $.get, $.ajax
- $.post, load, $.ajax

**45. In functie de context, in jQuery $(this) poate fi folosit pentru**
- a returna obiectul JavaScript de baza in jurul caruia este construit obiectul jQuery curent
- pentru a construi un wrapper jQuery in jurul documentului
- pentru a construi un wrapper jQuery in jurul obiectului window (in cazul in care suntem in scopul global)
- a construi un wrapper jQuery in jurul obiectului pe care se apeleaza un eveniment in interiorul functiei de tratare a evenimentului (pentru listenere)

**46. Care dintre urmatoarele informatii despre plugin-urile jQuery sunt adevarate:**
- Unele plugin-uri permit portarea codului jQuery pe diferite browsere
- Permit extinderea functionalitatii API-ului standard jQuery cu noi metode si functionalitati
- Unele plugin-uri jQuery permit incadrarea librariei de pe diversele CDN-uri (Content Delivery Network)
- Unele plugin-uri permit rularea codului jQuery independent

**47. Care dintre urmatoarele expresii jQuery se pot folosi interschimbabil?**
- $(selector).each(function() {}) cu $(selector.each).function() {}
- $(ready(function() {})) cu $(document).ready(function() {})
- $(function() {}) cu $(document).ready(function() {})

**48. Care dintre expresii jQuery se pot folosi interschimbabil?**
- $ si jQuery
- $("#myelem")[0] si document.getElementById("myelem")
- $("#myelem") si $(document.getElementById("myelem"))
- $(document.myelem) si $(document.getElementById("myelem"))
- $(document).myelem si $("myelem")[0]

**49. Care dintre urmatoarele reprezinta specificatii ale unor metode din API-ul jQuery?**
- ca pot fi folosite atat ca functii Setter cat si ca functii Getter
- ca pot fi apelate atat pe obiectul din DOM cat si pe wrapperul jQuery construit in jurul lui
- ca intorc referinta la obiectul pe care au fost apelate
- ca pot fi apelate atat in mod sincron cat si in mod asincron


## AJAX / HTTP

**50. De pe client se doreste salvarea unor date despre o persoana (numele si varsta acesteia) folosind un apel AJAX realizat prin metoda HTTP GET. Care dintre urmatoarele apeluri realizeaza acest lucru:**
- $.get("save.php?nume=Ion&varsta=10")
- $.get("save.php", { nume: 'Ion', varsta:10 })
- $.get("save.php", 'nume=Ion&varsta=10')

**51. Un apel AJAX se poate face catre o resursa statica oferita de end-point-ul de pe server?**
- Doar daca in QUERY_STRING / body-ul POST-ului se trimit spre server si parametri
- Da, intotdeauna
- Nu, niciodata

**52. Ce indica un cod de raspuns de forma 3xx trimis prin intermediul protocolului HTTP de un server web?**
- mutarea documentului cerut la o alta adresa
- o eroare efectuata de client
- redirecteaza clientul spre alt URL
- o eroare aparuta pe partea de server

**53. Pentru a redirectiona automat browser-ul spre un nou URL, server-ul poate:**
- raspunde clientului cu un cod de raspuns de forma 1xx impreuna cu un header Location
- trimite ca si continut HTML clientului un link de forma &lt;a href="http://url-nou"&gt; si simuleaza din JavaScript un click de mouse pe acest link
- include noul URL direct pe back-end folosind o directiva include
- redirectioneaza clientul folosind o secventa de cod in JavaScript de forma window.location = "http://url-nou"

**54. In ce conditii metoda send pe un obiect AJAX se apeleaza intotdeauna cu parametrul sirul vid?**
- Daca requestul AJAX se face prin GET
- Daca requestul AJAX se face prin HTTP
- Metoda send nu se poate apela cu parametrul sirul vid

**55. Pentru a verifica succesul unui apel AJAX trebuie ca:**
- readyState sa fie 4 si request.status 100
- readyState sa fie 0 si request.status 100
- readyState sa fie 4 si request.status 200
- readyState sa fie 1 si request.status 200

**56. Care dintre urmatoarele reprezinta proprietati membre ale unui obiect de tipul XMLHttpRequest:**
- readyState, response, responseText, status
- state, request, response, status
- readyState, request, response, status

**57. Un apel AJAX este in starea 2 daca:**
- s-a facut receive, dar nu s-a facut send
- s-a facut si send si receive, chiar inainte de close
- s-a facut open dar inca nu s-a facut send
- s-a facut send dar inca nu a sosit raspunsul de la server

**58. Functia specificata ca si valoare pentru proprietatea onreadystatechange se apeleaza**
- Atunci cand obiectul AJAX isi schimba starea
- Este posibil sa se apeleze si atunci cand obiectul nu isi schimba starea dar continua sa vina raspuns de la server
- Atunci cand serverul schimba codul de raspuns (response code-ul) care e trimis clientului

**59. Raspunsul sosit printr-un apel AJAX poate fi:**
- un fisier text
- un fisier JSON
- un fisier XML
- un fisier JavaScript

**60. Un URL de pe back-end poate fi invocat (cerut) atat prin GET cat si prin POST:**
- Doar daca datele primite fie prin GET fie prin POST sunt aceleasi
- Da
- Nu

**61. Care dintre urmatoarele metode HTTP nu presupun trimiterea de continut dupa antete in cadrul unui raspuns HTTP:**
- GET
- PUT
- POST
- HEAD

**62. Care dintre urmatoarele metode HTTP nu presupun trimiterea de continut dupa antete in cadrul unei cereri HTTP:**
- GET
- POST
- PUT
- HEAD

**63. Pe ce nivel al stivei TCP/IP se situeaza protocolul HTTP?**
- Aplicatie
- Internet
- Legatura de date
- Transport


## PHP

**64. Un fisier PHP poate fi executat:**
- In linia de comanda folosind interpretorul php.exe pe Windows sau php pe Linux
- De catre browser (PHP ruleaza server-side)
- De catre interpretatorul de comenzi al sistemului de operare pe care este instalata stiva AMP
- De catre un modul din cadrul serverului web

**65. Dandu-se doua variabile $a si $b, cum se poate interschimba valoarea acestora in PHP?**
- list($a, $b) = ($b, $a);
- array($a, $b) = ($b, $a);
- $a = $b; $b = $a;
- array($a, $b) = [$b, $a];
- list($a, $b) = [$b, $a];
- array($a, $b) = array($b, $a);
- array($a, $b) = list($b, $a);
- list($a, $b) = array($b, $a);
- list($a, $b) = list($b, $a);

**66. In PHP return este folosit pentru:**
- a termina scriptul curent care se executa si a reda controlul executiei unui eventual script care l-a inclus pe scriptul curent cu include sau require
- a termina executia logicii ce se executa pe server si a trimite raspunsul complet clientului
- a trimite clientului un anumit raspuns in urma cererii facute de acesta
- a termina executia unei functii

**67. Fie doua variabile $a si $b in PHP ce contin siruri de caractere. Aceste doua siruri in PHP se pot concatena folosind:**
- $s = "$a$b";
- $s = $a . $b;
- $s = '$a$b';
- $s = $a + $b;

**68. Care dintre urmatoarele declaratii este corecta pentru definirea tabloului $fructe in PHP?**
- $fructe=array("struguri", "mere", "pere");
- $fructe=("struguri", "mere", "pere");
- $fructe="struguri", "mere", "pere";
- $fructe={"struguri", "mere", "pere"};
- $fructe=array["struguri", "mere", "pere"];

**69. Unde se poate folosi functia header in PHP?**
- in primul scriptlet din fisierul PHP si doar cu conditia ca scriptul PHP sa nu fi trimis anterior spre browser orice forma de continut (cu exceptia unor eventuale antete)
- in cadrul unui scriptlet PHP plasat in sectiunea documentului HTML generat de fisierul PHP
- in orice scriptlet din cadrul fisierului PHP

**70. Cum se introduc comentarii intr-un scriptlet PHP**
- //
- &lt;!-- --&gt;
- /* ... */
- #

**71. Ce face functia session_start() in PHP?**
- Daca este vorba de o sesiune noua, genereaza un cookie aleator de sesiune pe care il trimite folosind antetul HTTP clientului
- Initializeaza intotdeauna o noua sesiune
- Porneste sesiunea existenta deja si trimite clientului cookie-ul de sesiune prin intermediul antetului HTTP Cookie
- Daca este vorba de o sesiune veche, determina despre ce sesiune este vorba pe baza valorii cookie-ului de sesiune trimis de client prin intermediul antetului HTTP Cookie si populeaza tabloul $_SESSION in mod corespunzator


## Securitate web (XSS, SQL Injection, sesiuni)

**72. Care dintre urmatoarele reprezinta masuri pentru evitarea vulnerabilitatilor de tip XSS**
- Verificari riguroase la nivelul browserului legate de validitatea datelor introduse
- Folosirea la nivelul browserului a unor biblioteci de functii JavaScript consacrate si testate anterior
- Inlocuirea anumitor caractere din datele primite de la client cu entitatile HTML corespunzatoare
- Dezactivarea din cadrul aplicatiei web a posibilitatii rularii de cod JavaScript de catre browser

**73. Injectiile JavaScript se datoreaza:**
- Folosirii protocolului http in locul protocolului https
- Validarii insuficiente chiar la nivelul codului JavaScript
- Unor buguri prezente la nivelul browserului web
- Validarii insuficiente server-side la nivelul scriptului ce prelucreaza datele din formular

**74. Care dintre urmatoarele reprezinta masuri pentru evitarea injectiilor SQL:**
- Dezactivarea in cadrul aplicatiei Web a posibilitatii rularii de cod SQL de catre browser
- Verificarile riguroase la nivelul backend-ului legate de validitatea datelor introduse precum si folosirea de biblioteci specializate pentru persistenta datelor (ORM-uri)
- Verificari riguroase la nivelul browserului legate de validitatea datelor introduse
- Folosirea la nivelul backend-ului de mecanisme de tipul "prepared statement"

**75. Pentru evitarea injectiilor SQL in PHP 7 se recomanda:**
- Eliminarea ghilimelelor si apostrofelor din datele primite de la client
- Evitarea acestora folosind functia mysql_real_escape_string
- Folosirea de "prepared statement"-uri

**76. Care dintre urmatoarele afirmatii sunt adevarate:**
- MySQL Old Extension (mysql_*) nu suporta "prepared statement"-uri, dar permite evitarea injectiilor SQL
- MySQL Improved Extension (mysqli_*) asigura in plus fata de PDO posibilitatea folosirii de "prepared statement"-uri
- MySQL Improved Extension (mysqli_*) asigura in plus fata de PDO independenta fata de sistemul de gestiune al bazelor de date folosit

**77. Cum se poate "fura" un cookie de sesiune al unui alt utilizator?**
- Prin lipsa invalidarii sesiunii (logout) si navigarea in continuare pe un site malitios
- Prin intermediul unui cod JavaScript injectat de catre atacator (XSS)
- Prin interceptarea datelor la nivelul retelei de transport in lipsa folosirii unei conexiuni sigure (man in the middle)

**78. Care dintre urmatoarele functii PHP este folosita pentru a preveni vulnerabilitatile de tip Cross-Site Scripting (XSS)?**
- mysqli_real_escape_string
- htmlentities
- filter_xss

**79. Care dintre urmatoarele vulnerabilitati ar putea fi exploatata pentru a fura sesiunea unui utilizator autentificat?**
- Cross-Site Request Forgery (CSRF)
- SQL Injection
- Cross-Site scripting (XSS)

**80. Un client face o cerere iar serverul ii raspunde cu "404 Not Found". Care dintre urmatoarele afirmatii sunt adevarate:**
- Chiar daca nu a gasit fisierul cerut de client, serverul ii poate da totusi acestuia un anumit continut HTML
- Daca nu a gasit fisierul cerut, serverul nu ii mai poate returna clientului nimic dupa codul de raspuns 404
- Chiar daca serverul a raspuns cu "404 Not Found", browserele raspund cu "200 Ok" pentru a-i spune serverului ca au primit mesajul de eroare


## Java Web (Servlets, JSP)

**81. Conectarea la o baza de date intr-o aplicatie web Java se recomanda a fi facuta:**
- La fiecare request in cadrul metodei doGet sau doPost
- La initializarea aplicatiei web / a contextului
- La pornirea containerului, o singura conexiune pentru toate aplicatiile web

**82. Parametrii de configurare ai unei aplicatii web Java sunt preluati din fisierul de configurare al aplicatiei web in cadrul metodei:**
- contextInitialized()
- applicationCreated()
- init()

**83. O baza de date embedded in cadrul unei aplicatii web Java se recomanda a fi memorata:**
- Oriunde altundeva in cadrul sistemului de fisiere dar nu in cadrul directorului aplicatiei web intrucat de acolo baza de date ar putea fi accesata de catre clientii web
- In cadrul folderului app/databases/, unde app reprezinta directorul (numele) aplicatiei web
- In folderul WEB-INF al aplicatiei web intrucat acesta nu este accesibil de catre un client web

**84. In cate instante se instantiaza un servlet?**
- Intr-o singura instanta
- In nici una, un servlet este executat direct de catre container/application server
- Se creeaza o instanta separata la fiecare cerere facuta de catre un client

**85. O sesiune intr-o aplicatie web Java se termina cu:**
- session.destroy()
- session.close()
- session.invalidate()


## Servere web (general)

**86. Care dintre urmatoarele sunt adevarate despre un server web:**
- un server web poate fi configurat sa accepte cereri prin protocolul HTTP pe portul 443
- un server web poate fi configurat sa accepte cereri printr-un URL de forma file://...
- implicit un server web asteapta cereri prin protocolul HTTP pe portul 80 si cereri prin protocolul HTTPS pe portul 443

