<h1>Mobilná aplikácia pre podporu výučby predmetu Rozvrhovanie a logistika</h1>

<p>Aplikácia „Mobilná aplikácia pre podporu výučby predmetu Rozvrhovanie a logistika“ je vytvorená ako bakalárska práca. Poskytuje študentom možnosť študovať predmet Rozvrhovanie a logistika, pričom im umožňuje pracovať s praktickými úlohami, sledovať vlastný pokrok a získavať odpovede na otázky súvisiace s predmetom.</p>

<h2>Obsah</h2>
<ul>
    <li><a href="#funkcionalita">Funkcionalita aplikácie</a></li>
    <li><a href="#install">Inštalácia a spustenie</a></li>
    <li><a href="#struktura">Štruktúra projektu</a></li>
    <li><a href="#architektura">Architektúra aplikácie</a></li>
    <li><a href="#firebase">Firebase integrácia</a></li>
    <li><a href="#kodova-baza">Kódová báza</a></li>
    <li><a href="#technologie">Použité technológie</a></li>
</ul>

<h2 id="funkcionalita">Funkcionalita aplikácie</h2>
<ul>
    <li>Registrácia a prihlásenie používateľov</li>
    <li>Správa používateľského profilu</li>
    <li>Zobrazovanie vzdelávacích materiálov</li>
    <li>Riešenie úloh a testovanie vedomostí</li>
    <li>Monitoring pokroku študenta</li>
    <li>Fórum na otázky a odpovede</li>
    <li>Možnosť kontaktovať učiteľa</li>
</ul>

<h2 id="install">Inštalácia a spustenie</h2>
<ol>
    <li>Stiahnite alebo klonujte projekt z <a href="https://github.com/LukaKuzyk/BakalarskaPraca">GitHub repozitára</a>.</li>
    <li>Otvorte projekt v Android Studio.</li>
    <li>Nainštalujte závislosti a synchronizujte projekt.</li>
    <li>Nakonfigurujte Firebase pre Android aplikáciu podľa <a href="https://firebase.google.com/docs/android/setup">oficiálnej dokumentácie</a>.</li>
    <li>Spustite aplikáciu na emulátore alebo fyzickom zariadení.</li>
</ol>

<h2 id="struktura">Štruktúra projektu</h2>
<pre>
<code>
BakalarskaPraca/
│
├── .idea/                      # Konfiguračné súbory pre Android Studio
├── app/
│   ├── build/                  # Vygenerované súbory build procesu
│   ├── src/
│   │   └── main/
│   │       ├── java/com/luka_kuzyk/bakalarskapraca/
│   │       │   ├── LoginActivity.kt
│   │       │   ├── RegisterActivity.kt
│   │       │   ├── ForgotPassword.kt
│   │       │   └── ...
│   │       └── res/
│   │           └── layout/
│   │               └── activity_login.xml
│   │               └── activity_register.xml
│   │               └── activity_forgot_password.xml
│   │               └── ...
│   └── build.gradle
├── gradle/                     # Konfiguračné súbory pre Gradle
├── .gitignore
└── build.gradle
</code>
</pre>

<h2 id="architektura">Architektúra aplikácie</h2>
<p>Aplikácia je vyvinutá v jazyku Kotlin v súlade s architektonickým štýlom MVVM (Model-View-ViewModel). Použité sú knižnice ako Firebase Auth pre autentifikáciu a Firestore pre ukladanie údajov.</p>

<h3>Model-View-ViewModel (MVVM)</h3>
<ul>
    <li><strong>Model:</strong> Triedy, ktoré definujú dátové štruktúry a poskytujú dáta z Firebase Firestore.</li>
    <li><strong>View:</strong> Aktivity a fragmenty, ktoré zobrazujú používateľské rozhranie a reagujú na interakcie používateľa.</li>
    <li><strong>ViewModel:</strong> Poskytuje dátam logiku a sprostredkováva ich medzi Model a View.</li>
</ul>

<h2 id="firebase">Firebase integrácia</h2>
<p>Aplikácia využíva Firebase pre autentifikáciu a ukladanie dát. Pre jej konfiguráciu je potrebné:</p>
<ol>
    <li>Pridať aplikáciu do <a href="https://console.firebase.google.com/">Firebase konzoly</a>.</li>
    <li>Stiahnuť konfiguračný súbor <code>google-services.json</code> a pridať ho do priečinka <code>app/</code>.</li>
    <li>Pridať Firebase závislosti do súboru <code>build.gradle</code>.</li>
</ol>

<h2 id="kodova-baza">Kódová báza</h2>

<h3>LoginActivity.kt</h3>
<pre><code>
package com.luka_kuzyk.bakalarskapraca

// Import potrebných knižníc
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

// Definícia triedy LoginActivity, ktorá dedí od AppCompatActivity
class LoginActivity : AppCompatActivity() {

    // Deklarácia premenných pre ovládacie prvky UI
    lateinit var emailInput: TextInputEditText
    lateinit var passwordInput: TextInputEditText
    lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth  // Firebase authentification

    // Metóda onCreate sa volá pri vytváraní aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)  // Nastavenie rozloženia obsahu

        // Pripojenie ovládacích prvkov z rozhrania k objektom v kóde
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)

        // Inicializácia Firebase autentifikácie
        auth = FirebaseAuth.getInstance()

        // Nastavenie onClickListener pre tlačidlo loginButton
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Kontrola, či bolo pole emailu vyplnené
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext, "Zadajte email a heslo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prihlásenie používateľa s emailom a heslom
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Prihlásenie úspešné", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Prihlásenie zlyhalo", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
</code></pre>

<h3>RegisterActivity.kt</h3>
<pre><code>
package com.luka_kuzyk.bakalarskapraca

// Import potrebných knižníc
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

// Definícia triedy RegisterActivity, ktorá dedí od AppCompatActivity
class RegisterActivity : AppCompatActivity() {

    // Deklarácia premenných pre ovládacie prvky UI
    lateinit var emailInput: TextInputEditText
    lateinit var passwordInput: TextInputEditText
    lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth  // Firebase authentification

    // Metóda onCreate sa volá pri vytváraní aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)  // Nastavenie rozloženia obsahu

        // Pripojenie ovládacích prvkov z rozhrania k objektom v kóde
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        registerButton = findViewById(R.id.register_button)

        // Inicializácia Firebase autentifikácie
        auth = FirebaseAuth.getInstance()

        // Nastavenie onClickListener pre tlačidlo registerButton
        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Kontrola, či bolo pole emailu vyplnené
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext, "Zadajte email a heslo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registrácia používateľa s emailom a heslom
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Registrácia úspešná", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Registrácia zlyhala", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
</code></pre>

<h3>ForgotPassword.kt</h3>
<pre><code>
package com.luka_kuzyk.bakalarskapraca

// Import potrebných knižníc
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

// Definícia triedy ForgotPassword, ktorá dedí od AppCompatActivity
class ForgotPassword : AppCompatActivity() {

    // Deklarácia premenných pre ovládacie prvky UI
    lateinit var fpEmail: TextInputEditText
    lateinit var fpBtn: Button
    private lateinit var auth: FirebaseAuth  // Firebase authentification

    // Metóda onCreate sa volá pri vytváraní aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)  // Nastavenie rozloženia obsahu

        // Pripojenie ovládacích prvkov z rozhrania k objektom v kóde
        fpBtn = findViewById(R.id.forgot_password_btn)
        fpEmail = findViewById(R.id.email)

        // Inicializácia Firebase autentifikácie
        auth = Firebase.auth

        // Nastavenie onClickListener pre tlačidlo fpBtn
        fpBtn.setOnClickListener {
            val email = fpEmail.text.toString()  // Získanie emailu z textového poľa

            // Kontrola, či bolo pole emailu vyplnené
            if(email.isEmpty()){
                Toast.makeText(applicationContext, "Zadajte email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Skorý návrat z listenera, ak nie je email zadaný
            }

            // Posielanie žiadosti o reset hesla
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    // Úspešné resetovanie hesla
                    Toast.makeText(applicationContext, "Heslo bolo úspešne resetované", Toast.LENGTH_SHORT).show()
                    finish()  // Ukončenie aktivity
                }.addOnFailureListener {
                    // Chyba pri resetovaní hesla
                    Toast.makeText(applicationContext, "Nastala chyba", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
</code></pre>

<h2 id="technologie">Použité technológie</h2>
<ul>
    <li><strong>Kotlin:</strong> Programovací jazyk pre Android vývoj</li>
    <li><strong>Android Studio:</strong> IDE pre Android vývoj</li>
    <li><strong>Firebase Auth:</strong> Autentifikácia používateľov</li>
    <li><strong>Firebase Firestore:</strong> Ukladanie údajov</li>
    <li><strong>Material Design Components:</strong> UI komponenty</li>
</ul>

<p>Autor: Luka Kuzyk</p>
