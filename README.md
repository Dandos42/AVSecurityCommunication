# AVSecurityCommunication

## Popis
Tento projekt byl vytovřen v rámci bakalářské práce. Projekt představuje aplikaci skládájící se ze dvou samostatných programů, které lze implementovat na dvě různá zařízení například na stolní počítač a notebook. Program TeleoperateServer simuluje teleoperační centrum, zatímco program AVclient představuje autonomní vozidlo. Jedná se o konzolovou aplikaci bez grafického rozhraní. Architektura je typu klient-server. Teleoperační centrum systému slouží k odesílání binárních souborů, které představují příkazy a data. Jeho úkolem je předávat tyto příkazy klientovi čili autonomnímu vozidlu prostřednictvím zvoleného protokolu. Klient tyto soubory následně uloží. Na této programové logice jsou následně implementovány protokoly MQTT a CoAP. 

## Obsah 
 - TeleoperateServer: Program simulující teleopreační centrum (server) pro vzdálené řízení vozidel
 - AVClient: Program simulující autonomní vozidlo (klienta), jež se připojuje k teleoprečnímu centru
 - CertECDSA: Certifikáty a klíče certifikační autority, MQTT brokera Mosquitto, MQTT klientů, CoAP serveru a Coap klienta vytvořených algoritmem ECDSA s eliptickou křivkou prime256v1 a hashovací funkcí sha256 ve formátu .crt a p.12
 - CertRSA: Certifikáty a klíče certifikační autority, MQTT brokera Mosquitto, MQTT klientů, CoAP serveru a Coap klienta vytvořených algoritmem RSA 2048 b ve formátu .crt a p.12
 - mosquitto: Konfigurační soubor Brokera Mosquitto, certifikáty brokera a soubor passwd obsahující přihlašovací údaje
 - Text_Prace.pdf : Text bakalářské práce 

## Instalace 
Nejprve je nutné stáhnutí aplikace, vložení programů na vhodné zařízení například PC a notebook a vzájemné propojení těchto zařízení přes přepínač. Programy je potřeba naimporotvat do vhodného vývojového prostředí doporučeno je vývojové prostředí Eclipse a přidat potřebné jar soubory knihoven do závislostí a classpath projektu včetně běhové konfigurace. Aby spojení fungovalo je nutné také nakonfigurovat vhodné IP adresy včetně masky podsítě, a pokud je potřeva upravit příchozí a odchozí pravidla firewallu. Výchozí adresy jsou IP adresa 192.168.10.2 pro teleoprační centrum a IP adresa 192.168.10.3 pro autonomní vozidlo. 
## Odkazy
- Eclipse Mosquitto: https://www.eclipse.org/paho](https://mosquitto.org/download/
- Eclipse Paho: https://github.com/eclipse/paho.mqtt.java
- Eclipse Californium: https://github.com/eclipse-californium/californium
- Vývojové prostředí Eclipse: https://www.eclipse.org/downloads/

## Použití
Po spuštění programu, je uživatel přivítán v menu s několika možnostmi. První možnost zobrazuje základní informace o projektu, včetně jména autora a popisu projektu. Tato sekce umožňuje uživateli získat základní přehled o projektu. Druhá a třetí možnost umožňují uživateli spustit nezabezpečenou nebo zabezpečenou komunikaci pomocí MQTT. Pokud uživatel vybere možnost druhou dojde
k navázání nezabezpečené komunikace klienta publishera nebo subsribera s MQTT brokerem. Volba závisí na tom, jestli je spuštěn program pro teleoperační centrum nebo autonomní vozidlo. Třetí volba na rozdíl od druhé pracuje se zabezpečeným spojením za použití protokolu TLS. Čtvrtá a pátá volba menu umožňují uživateli spustit komunikaci pomocí CoAP.
Opět zde máme možnost nezabezpečeného a zabezpečeného spojení, kde zabezpečené spojení využívá protokol DTLS. Kód programu TeleoperateServer představuje CoAP server. Naopak kód programu AVClient simuluje roli 
CoAP klienta, který se připojuje k serveru.
## Licence
Tento projekt je licencován pod Eclipse Public License 2.0. Obsahuje knihovnya konfigurační soubor MQTT brokera Eclipse Mosquitto licencované pod následujícími licencemi:
- Eclipse Californium: [EPL 2.0]
- Eclipse Mosquitto: [EPL 2.0]
- Eclipse Paho: [EPL 2.0]

## Autor
- Daniel Prachař
- Univerzita: Vysoké učení technické v Brně
- Fakulta: Fakulta elektrotechniky a informatiky 
- Obor: Informační bezpečnost

## Kontakt
Pokud máte dotazy nebo připomínky, můžete mě kontaktovat na e-mail: 240969@vutbr.cz
