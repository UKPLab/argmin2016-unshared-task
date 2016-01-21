# Unshared Task for 3rd Workshop on Argument Mining, ACL 2016, Berlin

http://argmining2016.arg.tech/

See Call for paper: XXX

We provide four variants of the task across various registers. Each variant is split into three parts:

- **Development set:** We encourage your to perform the exploratory analysis, task definition, annotation experiments, etc. on this set.
- **Test set:** This small set might serve as a benchmark for testing your annotation model (or even a computer model, if you go that far) and reporting agreement measures (if applicable).
- **Crowdsourcing set:** a bit larger set if you plan any crowdsourcing experiments

Note that these various splits are only a recommendation and not obligatory, you are absolutely free to use the entire dataset if your task requires so.

## Variants

- Variant A: Debate portals
  - 8 devel files
  - 2 test files
  - 18 crowdsourcing files

- Variant B: Debate transcript
  - Opening and closing speech of the same speaker from Intelligence^2 debates
    - The entire debate transcripts would be extremely long for the purposes of the unshared task
  - 3 devel files
  - 2 test files
  - 5 crowdsourcing files

- Variant C: Opinionated newswire article
  - 8 devel files
  - 2 test files
  - 18 crowdsourcing files

- Variant D: Discussion under opinionated articles
  - 8 devel files
  - 2 test files
  - 18 crowdsourcing files
  
## Data

Data are stored in plain text format (UTF-8 encoding). The name of each file consists of the variant name, the sub-set, and the number in the subset, for example
``Bd002.txt`` is a ``B`` category file from the ``d`` development set with number ``2``. 