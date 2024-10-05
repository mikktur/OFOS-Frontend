# **Online Food Ordering System**

Tämä projekti on luotu simuloimaan OFOS-applikaatiota, jossa käyttäjä voi selata ravintoloita, valita tuotteita tilattavaksi ja suorittaa tilauksen maksun. Projektin tavoitteena oli tutustua ja oppia Agilen käyttöä Scrumia hyödyntäen
sekä hyödyntämään CI/CD:tä ohjelmistotuotannossa. Projektin käyttöliittymä toteutettiin JavaFX kirjastolla ja [rajapinta](https://github.com/mikktur/OFOS-Backend) kehitettiin Spring Boot kehyksellä. Projektin testit suoritettiin JUnitilla
ja testien tulosten raportointi Jacocolla. Projektin ulkoasu suunniteltiin Figmassa.

___

## Sovelluksen käyttö

Sovelluksen avatessa käyttäjä voi luoda tilin tai kirjautua sisään. Sisäänkirjautumisen jälkeen käyttäjä näkee näkymän, jossa on listattuna kaikki sovellukseen luodut ravintolat. Käyttäjä voi rajata ravintoloita muun muassa ravintolan nimen 
tai ravintolan tuotteiden perusteella. Näkymästä löytyy painike, josta käyttäjä pääsee muokkaamaan omia tietojaan ja lisäämään toimitusosoitteita tililleen. Ravintolan valittua käyttäjälle näytetään kaikki ravintolan tuotteet ja niiden tiedot.
Käyttäjä voi valita ravintolan menusta tuotteen ja lisätä sen ostoskoriin. Tämän jälkeen ostoskorista valitsemaan toimitusosoitteen ja pääsee maksamaan tilauksen.

Ravintolan omistaja voi päivittää ravintolansa tuotteita, yhteystietoja ja tuotteiden hintoja sekä lisätä tai poistaa tuotteita ravintoloistaan.

___

## Testit

Testit suoritettiin JUnitilla, joita käytettiin projektissa CI/CD:tä varten Jenkinsissä. 

>Kuva jacoco raportista (?)
