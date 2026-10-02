1. Un apel AJAX se poate face catre o resursa statica oferita de end-point-ul de pe server?
Doar daca in QUERY STRING/body-ul POST-ului se trimit spre server si parametri
X. Da, intotdeauna
Nu, niciodata

2. #RRAABB este un cod de culoare valid?
Nu, deoarece codul de culoare hexazecimal trebuie sa contina inca doua cifre pentru opacitate 
Da, deoarece valorile sunt specificate in baza 16
Da, deoarece RR specifica cantitatea de rosu, AA cantitatea de verde si BB cantitatea de albastru 
X. Nu, deoarece valorile nu sunt specificate in baza 16

3. Un formular care contine un input de tip file trebuie:
submis prin metoda GET
X. submis prin metoda POST
sa aiba specificat atributul accept care specifica tipul fisierelor ce se pot trimite la server
X. sa aiba specificat atributul enctype setat la multipart/form-data
sa aiba specificat atributul content care sa indice spre continutul fisierului

4. Care dintre urmatoarele afirmatii referitoare la filtrele de vizibilitate sunt adevarate?
X. Selectorul :hidden se foloseste pentru a selecta toate elementele ascunse in pagina
Selectorul :visible se foloseste pentru a selecta elementele care au display:none sau width/height:0 
Selectorul :hidden se foloseste pentru a selecta toate elementele care ocupa spatiu in pagina 
X. Selectorul :visible se foloseste pentru a selecta toate elementele care ocupa spatiu in pagina 
Selectorul :visible se foloseste pentru a selecta elementele care au visibility:hidden si opacity:0
> teoretic face si asta, dar nu este facut pentru asta...

5. Ce indică un cod de raspuns de forma 3xxx trimis prin intermediul protocolului HTTP de un server web?
X. mutarea documentului cerut la o alta adresa
o eroarea efectuata de client
X. redirecteaza clientul spre alt URL
o eroare aparuta pe partea de server


6. De pe client se doreste salvarea unor date despre o persoana (numele si varsta acesteia) folosind un apel AJAX realizat prin metoda HTTP GET. Care dintre urmatoarele apeluri realizeaza acest lucru:
X. $.get("save.php", {nume: "lon", varsta: 10 })
X. $.get("save.php?nume=lon&varsta=10")
X. $.get("save.php", 'nume=lon&varsta=10')

7. Ce face urmatoarea secventa de cod $(li:first).addClass('patrat').addCllass('deplasat').addClass('colorat')
O selecteaza toate listele si se adauga clasele patrat deplasat colorat
selecteaza primul element din fiecare lista si adauga clasele patrat, deplasar, colorat 
selecteaza toate elementele din liste si le adauga clavele patrat, deplasat, colorat 
X. selecteaza primul li si adauga clasele patrat, deplasat, colorat

8. Un URL de pe back-end poate fi invocat (cerut) atat prin GET cat si prin POST:
Nu
X. Da
Doar daca datele primite fie prin GET fie prin POST sunt aceleasi (parametrii trimisi sunt aceeasi)

9. Un fisier PHP poate fi executat:
X. In linia de comanda folosind interpretorul php.exe pe Windows sau php pe Linux
De catre browser
De catre interpretorul de comenzi al sistemului de operare pe care este instalata stiva AMP
X. De catre un modul din cadrul serverului web

10. Care dintre urmatoarele afirmatii referitoare la AJAX sunt adevarate?
X. AJAX este o tehnologie care permite efectuarea de apeluri asincrone catre server
X. AJAX permite actualizarea partiala a unei pagini web fara a fi nevoie de reincarcarea intregii pagini

11. Cum se declara un array vid in PHP:
X. $emptyArray = [];
X. $emptyArray = array();
$emptyArray = null;
$emptyArray = {};

12. Ce face urmatoarea secventa de cod $(':not(p)').addClass('patrat').addClass('colorat');
X. selecteaza toate elementele cu exceptia paragrafelor si le adauga clasele patrat si colorat 
selecteaza toate elementele care au clasa patrat si clasa colorat cu exceptia paragrafelor 
selecteaza toate elementele cu exceptia paragrafelor care au clasa patrat si clasa colorat 
selecteaza toate paragrafele care au clasa patrat si clasa colorat

13. Pentru a defini o listă ordonata a căror itemi sunt precedați de litere din alfabetul grec în CSS se foloseşte:
list-type: lower-greek
X. list-style-type: lower-greek
list: lower-greek
X. list-style: lower-greek;

14. Pentru a redirectione automat browser-ul spre un nou URL, server-ul poate
X. raspunde clientului cu un cod de raspuns de forma 302 impreuna cu un header Location
trimite ca si continut HTML clientului un link de forma <a href="http://url-nou">URL nou</a> si simuleaza din lavascript un click de mouse pe acest link
> teoretic posti... dar nu stiu daca proful considera corect raspunsul asta
include noul URL direct pe back-end folosind o directiva include
> inserează conținutul unui alt fișier/resurse în pagina curentă, la nivel de server, fără să schimbe URL-ul din bara de adrese a browserului
X. redirectioneaza clientul folosind o secventa de cod in JavaScript de forma window.location="http://url-nou"

15. Care dintre urmatoarele tag-uri HTML sunt deprecated in HTML5:
X. center
X. font
b
body

16. Cum se inserează corect un fisier extern JavaScript denumit test.js într-un document HTML?
<script name="test.js"></script>
<script src="test.js">
<script href="test.js">
X. <script src="test.js"></script>

17. Pentru inserarea de diacritice intr-un document HTML se pot folosi
X. Editoare capabile sa salveze fisierul UTF-8 si specificarea acestui set de caractere in sectiunea head a documentului HTML
X. Entitati HTML
Tastatura setata pe limba romana si specificarea atributului lang="ro-RO" la tagul HTML

18. Care sunt asemanarile si diferentele dintre atributele id si name?
atributul name se foloseste pentru referirea unui element din CSS/JavaScript
X. doua elemente HTML pot avea acelasi nume, dar nu pot avea acelasi id
X. orice tag accepta atibutul id, nu toate tagurile accepta atributul name
orice tag accepta atributul name, nu toate tagurile accepta atributul id
doua elemente HTML pot avea acelasi id, dar nu pot avea acelasi nume
X. atributul id se foloseste pentru referirea unui element din CSS/JavaScript

19. In JavaScript typeof 1/0 este NaN pentru ca:
type of 1/0 nu are rezultatul NaN, ci Infinity
X. type of 1 este Number si Number nu se poate imparti la 0, rezultatul fiind NaN
1/0 este egal cu Infinity si typeof Infinity este NaN
1/0 este NaN si typeof NaN este NaN

20. Ce se intelege prin scop global in JavaScript?
X. obiectul window
obiectul document
variabila this
scopul imediat exterior celui in care este declarata o functie
> scop lexical, nu scopul global. Scopul global este cel mai exterior nivel posibil

21. Aranjati in ordine, de la cea mai simpla si putin customizabila, la cea mai configurabila si parametrizabila urmatoarele functii care permit realizarea unui apel AJAX din jQuery.
$get, load, $.post
X. load, $.get, $.ajax
$.post, load, $.ajax
$.ajax, load, $.get

22. Care este forma corecta de folosire a tag-ului img in cadrul unui document XHTML:
<img>poza.jpg</img>
X. <img src="poza.jpg"/>
<img src="poza.jpg"></img>
<img src="poza.jpg">
> corect in HTML5, dar nu in XHTML, unde tag-ul img este un tag self-closing

23. Care dintre următoarele reprezintă măsuri pentru evitarea injecţilor SQL:
X. Verificări riguroase la nivelul backend-ului legate de validitatea datelor introduse precum si folosirea de biblioteci specializate pentru persitarea datelor (ORM-uri)
Dezactivarea in cadrul aplicaţiei Web a posibilitaţii rulării de cod SQL de către browser
Verificări riguroase la nivelul browserului legate de validitatea datelor introduse
X. Folosirea la nivelul backend-ului de mecansime de tipul "prepared statement"

24. Ce face urmatoarea secventa de cod:
```js
var content=$("li").html();
$('li').append('<em>'+ content + '</em>');
```
introduce continutului primului list item inaintea fiecarui list item
continutul din toate li-urile vor fi scrise cu italic
introduce content dupa primul list item
X. introduce continutului primului list item dupa fiecare list item
continutul din primul li va fi scris cu italic

25. Care dintre urmatoarele afirmatii despre metoda HTTP POST sunt adevarate: 
trebuie neaparat folosita pentru a submite un formular care contine un input de tip video 
> nu exista input de tip video
X. este recomandat a fi folosita pentru a submite un formular care contine un input de tip password 
X. trebuie neaparat folosita pentru a submite un formular care contine un input de tip file

26. Fie doua variabile $a si $b in PHP ce contin siruri de caractere. Aceste doua variabile pot fi concatenate folosind operatorul:
X. $s = $a . $b;
$s = $a + $b;
X. $s = "$a$b";
$s = '$a$b';

27. Pozitionarea relativa este folosita pentru:
X. a pozitiona/muta un element in diferite directii relativ la pozitia sa normala
a pozitiona un element relativ la obiectul document
a pozitiona un element relativ la fereastra browser-ului (obiectul window)
X. un container parinte care are elemente fiu pozitionate absolut

28. Care dintre urmatoarele metode HTTP nu presupun trimiterea de continut dupa antete in cadrul cererii HTTP:
X. GET
POST
PUT
X. HEAD
X. DELETE

29. In functie de context, in JQuery $(this) poate reprezenta:
a returna obiectul JavaScript de baza in jurul caruia este construit obiectul jQuery curent
pentru a construi un wrapper JQuery in jurul documentului
X. pentru a construi un wrapper JQuery in jurul window
X. a contrui un wrapper JQuery in jurul obiectului pe care se apeleaza un evenijment in interiorul functiei de tratare a evenimentului

30. Considerand urmatoarea secventa de cod:
```html
<div class="info">John Doe</div>
<div class="info number" id="age">30</div>
```
Ce selector CSS va selecta doar al doilea div?
X. .info.number
.info .number
.info #age
X. .info[id] 

32. In ce context metoda send pe un obiect AJAX este apelata intotdeauna cu un parametru null?
X. in cazul in care metoda HTTP folosita este GET
daca request-ul AJAX este prin HTTP
metoda send nu poate fi apelata cu un parametru null

33. Cum se ascund toate div-urile din pagina folosind jQuery?
X. $('div').hide();
X. $('div').css('visibility', 'hidden');
$('div').hidden();
$('div').visible(false);
$('div').css('visibility', 'hide');

34. Care dintre urmatoarele metode jQuery folosesc AJAX:
X. $.ajax, $.get, $.post, load
$.request, load, $.post
$.request, load, $.post, $.get
$.get, load, unload

35. Care dintre urmatoarele afirmatii sunt adevarate:
X. MySQL Old Extension (mysql_*) nu suporta "prepared statements"-uri, dar permite evitarea injecţiilor SQL prin folosirea funcţiei mysql_real_escape_string()
MySQL Improved Extension (mysqli_*) asigura in plus fata de PDO independenta data de sistemul de gestiune a bazelor de date (DBMS) folosit
X. MySQL Improved Extension (mysqli_*) asigura in plus fata de PDO posibilitatea folosirii de "prepared statements"-uri
> mysqli_real_escape_string() exista din PHP 8.x, dar mysql_real_escape_string() (FARA I) nu mai exista din PHP 7

36. In PHP return este folosit pentru:
X. a termina scriptul curent care se executa si a reda controlul executiei unui eventual script care l-a inclus pe scriptul curent cu include/require
a termina executia logicii ce se executa pe server si a trimite raspunsul complet clientului
a trimite clientului un anumit raspuns in urma cererii facute de acesta
X. a termina executia unei functii

37. Pe ce nivel al stivei TCP/IP se afla protocolul HTTP?
X. Aplicatie
Internet
Transport

38. Un apel AJAX este in starea 2 daca:
X. s-a facut send,, dar inca nu a fost primit raspuns de la server
s-a facut receive, dar nu s-a facut send
s-a facut si send si receive, chiar inainte de close
s-a facut open, dar nu s-a facut send

39. Elementul <th> trebuie folosit in cadrul unui tabel HTML pentru a defini:
X. celulele de tip header (cap de tabel)
definirea unui nou rand in tabel
X. centrarea continutului si bold-area textului dintr-o celula

40. Cum se declara corect un array in JavaScript?
X. var arr = ['a', 'b', 'c'];
X. var arr = new Array('a', 'b', 'c');
X. var arr = Array('a', 'b', 'c');
var arr = {'a', 'b', 'c'};

41. Pentru evitarea injectiilor SQL in PHP 7 se recomanda:
X. folosirea de "prepared statements"-uri
X. folosirea de ORM-uri (Object Relational Mapping)
folosirea functiei mysql_real_escape_string()

42. Injectiile JavaScript se datoreaza:
folosirii protocolului HTTP in loc de HTTPS
validarii insuficente chiar la nivelul codului JavaScript
unor buguri prezente la nivelul browser-ului web
X. validarii insuficente server-side a datelor primite de la client

43. Unde se poate defini un stil CSS pentru o anumita classa?
X. in interiorul tag-ului <style> din sectiunea <head> a documentului HTML
X. inline cu ajutorul atributului style al tag-ului HTML
X. intr-un fisier extern specificat cu ajutorul tag-ului <link> in sectiunea <head> a documentului HTML

44. Pentru a verifica succesul unui apel AJAX trebuie ca:
X. readyState sa fie 4 si status sa fie 200

45. Unde se poate insera cod JavaScript intr-un document HTML?
X. in interiorul tag-ului <script> din sectiunea <head> si in sectiunea <body> a documentului HTML

46. Fie urmatoarea secventa de cod:
// e secvaneta cu ce atribut CSS se utilizeaza, regula este:
selector de tip universal e.g. * = 0
selector de tip element e.g. div = 1
selector de tip clasa e.g. .info = 10
selector de tip id e.g. #age = 100
selector de tip inline e.g. style="..." = 1000

acestea se aduna pentru fiecare selector dintr-o regula CSS, e.g. pentru regula CSS .info #age {color: red;} specificitatea este 10 + 100 = 110, !important depaseste orice specificitate

47. Care dintre urmatoarele functii PHP sunt folosite pentru prevenirea vulnerabilitatilor de tipul XSS (Cross Site Scripting)?
X. htmlspecialchars()
X. htmlentities()
X. mysqli_real_escape_string()
filter_xss()

48. Cum se poate "fura" un cookie de sesiune al unui utilizator?
X. prin injectarea nui cod JS de catre actor
X. prin lipsa invalidarii sesiunii (logout) si anvigarea in continuare pe un site malitios
X. prin interceptarea datelor la nivelul retelei de transport in lipsa folosii unei conexiuni sigure

49. Care dintre urmatoarele vulnerabilitati ar putea fi exploatate pentru a fura cookie-ul de sesiune al unui utilizator?
X. XSS (Cross Site Scripting)
X. CSRF (Cross Site Request Forgery)
SQL Injection

50. Raspunsul sosit printr-un apel AJAX poate fi:
X. un fisier text
X. un fisier JSON
X. un fisier XML
X. un fisier JavaScript

51. Care dintre urmatoarele categorii de aplicatii se pot folosi de DOCTYPE pentru "intelegerea" mai buna a documentului:
servere web
X. browsere
X. motoare de cautare
X. IDE-uri

52. Care este diferenta dintre readonly si disabled in HTML?
X. readonly permite selectarea si copierea continutului, dar nu permite modificarea
X. valoarea unui input este trimisa la server in cazul readonly, dar nu si in cazul disabled

53. Care dintre urmatoarele informatii despre tagul <title> sunt adevarate?
X. este folosit in mare de catre motoarele de cautare
ese optional
X. este afisat in bara de titlu sau bookmark
nu exista tagul <title> in HTML5

54. Care dintre antetele HTTP sunt obligtorii:
X. Host
User-Agent
Content-Type
Cookie

55. Care dintre urmatoarele afirmatii sunt adevarate:
definirea de stil interne au prioritate fata de stilurile externe
o definitie de stil aplicata clasei "bate" ca prioritate definitia de stil aplicata id-ului
X. o proprietate din cadrul unui stil inline suprascrie o proprietate dintr-o definitie de stil aplicata id-ului

56. Ce face functia session_start() in PHP?
X. porneste o sesiune sau continua o sesiune existenta

57. Care dintre urmatoarele declaratii este corecta pentru definirea tabloului in PHP?
X. $array = array(1, 2, 3);
X. $array = [1, 2, 3];
$array = {1, 2, 3};

58. Care dintre urmatoarele informatii despre un URL accesat prin POST sunt adevarate?
X. Reaccesarea URL-ului presupune o confirmare din partea utilizatorului
URL-ul i se poate face bookmark
URL-ul poate fi partajat cu alti utilizatori

59. Care dintre urmatoarele sunt adevarate desore un server web?
X. poate fi configurat sa primeasca HTTP pe portul 443
poate fi configurat sa primeasca cerei printr-un URL de forma file://...
X. implicit asteapta cereri HTTP pe portul 80 si HTTPS pe portul 443

60. Cum se pot seta mai multe proprietati CSS folosind jQuery?
X. $(selector).style({"color": "red", "width": "100%", "height": "100%"}); 
$(selector).css([color: "red", width: "100%", height: "100%"]); 
$(selector).css({"color": "red", "width": "100%", "height": "100%"}); 
X. $(selector).css("color","red").css("width", "100%").css("height", "100%");

61. Care dintre urmatoarele coduri de eroare HTTP ar trebui returnat in cazul in care datele introduse de catre client sunt invalide?
X. 400
204
606
100
302
500

62. Ce va afisa urmatoarea secventa de cod Javascript?
```js
var a = ["cat", "dog"];
a.length = 0;
a.push("mouse"); console.log(a);
```
X. ["mouse"]
["cat", "dog", "mouse"]
["mouse", "dog"]