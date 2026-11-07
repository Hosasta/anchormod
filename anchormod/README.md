# Anchor Mod (Fabric 1.21.4)

Klawisze (zmienisz w Opcje > Sterowanie > Anchor Mod):
- V - Water Clutch (patrz w dol, trzymaj dowolny blok, miej wiadro z woda na hotbarze)
- G - AutoSprint (przelacznik)
- H - Fly (przelacznik; spacja = w gore, shift = w dol)

Budowanie:
1. Wymagane: JDK 21.
2. Brakuje gradle wrapper - skopiuj `gradlew`, `gradlew.bat` i folder `gradle/` z szablonu
   https://fabricmc.net/develop/template/ (wybierz 1.21.4) albo uruchom `gradle wrapper`.
3. `./gradlew build` -> gotowy jar w `build/libs/`.
4. Do testow: `./gradlew runClient`.

Dodawanie modulu: nowa klasa w `module/impl` rozszerzajaca `Module`, potem
`modules.add(new TwojModul());` w `ModuleManager.registerAll()`.
