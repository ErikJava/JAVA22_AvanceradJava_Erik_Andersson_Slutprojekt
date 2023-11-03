# AvjsSlutprojekt_V4

När programmet körs öppnas ett grafiskt användargränssnitt i Swing (GUI) som är huvudfönstret i programmet. Där finns olika grafiska element, exempelvis en "JProgressBar", Add/Remove-button och en Logg. 
Progressbar är färgad vilket ger en indikation om hur mycket arbete som har utförts. 
De olika Buttons gör det möjligt att lägga till eller ta bort producenter, som är de "arbetare" som utför uppgifter. 
Det finns även en indikator som visar hur många producenter som för närvarande är aktiva. Denna siffra justeras när nya producenter läggs till eller tas bort. 
Slutligen finns där en textruta (JTextArea) som fungerar som ett loggfönster. Här loggas olika händelser och meddelanden som är relevanta för programmet och de nyaste händelserna visas först för användaren.

Programmet använder en speciell "Log"-klass för att hantera loggning av händelser och meddelanden. Meddelandena skrivs till en fil som kallas "log.txt".
I loggen inkluderas tidsstämplar och information om olika händelser, exempelvis när en ny producent läggs till, när enheter produceras eller konsumeras samt visar den genomsnittliga produktionsintervallen över tid (10 sekunder).

En viktig del av programmet är hur producenter och konsumenter samarbetar. Producenttrådar ansvarar för att producera enheter och placera dem i en gemensam Buffer med jämna tidsintervall. Konsumenttrådar tar bort enheter från denna Buffer med liknande intervall.

Användaren kan interagera med programmet genom att använda knapparna "Add worker" och "Remove worker" för att öka eller minska antalet producenter som är aktiva. Antalet trådar ändras beroende på vad användaren gör för val. 
JProgressBar används för att visa status och färgen ändras beroende på värdet. 
Om värdet är lågt (under 10 %) visas den i rött, medan höga värden (över 90 %) visas i grönt för att ge en indikation om produktionens tillstånd. Om värdet befinner sig utanför dessa procent så visas den i grått.

Det finns också en speciell "Kontroller"-klass som tar hand om att initialisera och starta konsumenttrådar. Dessa konsumenter är de som tar bort enheter från Buffer och "konsumerar" dem.

Slutligen finns det en metod som körs regelbundet för att logga den genomsnittliga produktionen av enheter över en tidsperiod på 10 sekunder. Detta ger användaren insikt i hur snabbt produktionen går över tid.

Samtliga klasser är uppdelade i olika Packages för att ge användaren en bättre överblick.
