
# Training and Availability of Models

All trained Models are available online at **ToDo: Add link**.
Additionally, we have a detailed step-by-step guide at [src/deepct/README.md](src/deepct/README.md) on how we have trained all the DeepCT models used in the paper.


# Sampling

```
./src/main/bash/run-sampling.sh cc-16-07-v2
./src/main/bash/run-resampling.sh cc-16-07-v2

./src/main/bash/run-sampling.sh cc-17-04-v2
./src/main/bash/run-resampling.sh cc-17-04-v2

./src/main/bash/run-sampling.sh cc-18-13-v2
./src/main/bash/run-resampling.sh cc-18-13-v2

./src/main/bash/run-sampling.sh cc-20-05-v2
./src/main/bash/run-resampling.sh cc-20-05-v2

./src/main/bash/run-sampling.sh cc-21-04-v2
./src/main/bash/run-resampling.sh cc-21-04-v2


./src/main/bash/run-sampling-new.sh script-filtered-commoncrawl-2016-07-repartitioned

./src/main/bash/run-sampling-new.sh script-filtered-commoncrawl-2017-04-repartitioned
```

# Ideen-Backlog:

### Aufbereitung vom Anchor-Context für das ranking von Dokumenten (AnchorContext2Query):

- Ausgangspunkt:
  - Wir haben große Mengen parallelen Text: Anchor/AnchorContext <-> Queries
  - Sobald wir die Anchor-Texte veröffentlichen, könnte die Idee vielleicht naheliegend sein
  - Die Idee verbindet (ich glaube ganz natürlich) zwei aktuell laufende Abschlussarbeiten
  - Ich glaube diese Situation gab es so noch nicht, überall auf den Datensätzen, auf denen Anchor-Texte verfügbar sind, gibt es bis dato keine richtigen Query logs, oder umgedreht, dort wo es query logs gibt, gibt es meist nichts weiter dazu (weil für query logs oft alles sehr stark obfuskiert ist)

Obwohl Anchor-Texte sich als sehr Hilfreich für das Ranking von Dokumenten gezeigt haben, ist ein Großteil der Anchor-Texte "schwer verständlich", wodurch die Nützlichkeit von Anchor-Texten potentiell eingeschränkt wird (z.B.: Anchor-Texte die nur Stoppwörter wie "Hier", oder "hier klicken" enthalten). Anstatt den reinen Anchor-Text zu verwenden, extrahieren wir die, für den jeweiligen Anchor wichtigen, Wörter aus dem Kontext des Anchors (z.b.: "Der Lebenslauf von Barack Obama kann auf <a>Wikipedia</a> eingesehen werden." => "Lebenslauf Barack Obama"). Dazu verwenden wir einen einfachen Distant Supervision Ansatz basierend auf Click-Logs (ORCAS: 18 Millionen Einträge) und Anchor-Texten passend zum MS-MARCO Datensatz extrahiert aus dem Common Crawl. Für jeden Eintrag im Click Log der Form ("X Y Z", URL) der anzeigt, dass das Dokument URL zur Query "X Y Z" geklickt wurde extrahieren wir  alle Dokumente die einen Link auf URL enthalten, und innerhalb dessen Link-Context (50 Wörter vor und nach dem Link) die Wörter X Y Z enthalten sind. Daraus extrahieren wir Trainingspaar ("Anchor-Context", "X Y Z") welches für ein Distant-Supervision Verfahren verwendet werden kann, um zu lernen, dass aus dem Anchor Context der perfekte Text X Y Z extrahiert werden soll. Sobald ein Modell diese Aufgabe "hinreichend gut beherrscht", lassen wir damit die Anchor-Texte die indexiert werden "vorverbeiten", um bessere Texte aus dem Anchor-Context zu extrahieren. Dieses Verfahren hat den Vorteil, dass wir vermutlich die einzigen sind, die etwas in die Richtung machen können

![Potential Future Work](thesis/sketch-future-work.jpeg "Potential Future Work")

Potentielle Motivationen:

- Query logs sind schwer zu bekommen, also baue ich etwas, was mir aus frei verfügbaren Ressourcen Query Logs bauen kann (Ich habe große Mengen an parallelen Text, die eine Menge bekomme ich "for free", die andere nicht, kann ich also die freie Menge umwandeln in die andere?)
