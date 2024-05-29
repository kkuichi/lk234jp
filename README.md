<h1>Mobilná aplikácia pre podporu výučby predmetu Rozvrhovanie a logistika</h1>

<p>V nasledujúcej príručke sa zameriame na detailný opis systémovej stránky aplikácie "Mobilná aplikácia pre podporu výučby predmetu Rozvrhovanie a logistika".
    Cieľom aplikácie je poskytnúť študentom lepšiu podporu pri štúdiu tohto predmetu prostredníctvom mobilnej aplikácie.</p>


Na zrealizovanie praktickej časti záverečnej práce sme využili jazyk Kotlin, ktorý je moderným, vysoko výkonným programovacím jazykom určeným na vývoj mobilných aplikácií pre platformu Android. Kotlin ponúka množstvo funkcií, ktoré zjednodušujú vývoj a umožňujú vytvárať čistý, prehľadný a efektívny kód.


<h2 id="funkcionalita">Funkcionalita aplikácie</h2>
<ul>
    <li>Prehľad vzdelávacích materiálov</li>
    <li>Testovanie získaných vedomostí</li>
    <li>Monitoring vlastného pokroku študenta</li>
    <li>Registrácia a prihlásenie</li>
    <li>Automatická synchronizácia obsahu aplikácie so službou Firebase</li>
</ul>


<h2 id="architektura">Architektúra aplikácie</h2>
<p>Aplikácia je vyvinutá v jazyku Kotlin v súlade s architektonickým štýlom MVVM (Model-View-ViewModel). Použité sú knižnice ako Firebase Auth pre autentifikáciu a Firestore pre ukladanie údajov.</p>

<h3>Model-View-ViewModel (MVVM)</h3>
<ul>
    <li><strong>Model:</strong> Triedy, ktoré definujú dátové štruktúry a poskytujú dáta z Firebase Firestore.</li>
    <li><strong>View:</strong> Aktivity a fragmenty, ktoré zobrazujú používateľské rozhranie a reagujú na interakcie používateľa.</li>
    <li><strong>ViewModel:</strong> Poskytuje dátam logiku a sprostredkováva ich medzi Model a View.</li>
</ul>


<h2 id="technologie">Použité technológie</h2>
<ul>
    <li><strong>Kotlin:</strong> Programovací jazyk pre Android vývoj</li>
    <li><strong>Android Studio:</strong> IDE pre Android vývoj</li>
    <li><strong>Firebase Auth:</strong> Autentifikácia používateľov</li>
    <li><strong>Firebase Firestore:</strong> Ukladanie údajov</li>
    <li><strong>Material Design Components:</strong> UI komponenty</li>
</ul>

<p>Autor: Luka Kuzyk</p>

luka.kuzyk.dev@gmail.com
