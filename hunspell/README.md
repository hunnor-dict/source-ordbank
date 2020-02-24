Conversion to Hunspell file format

1 Introduction

Hunspell uses a format similar to Norsk ordbank.

* A .aff file defines the rules
* A .dic file lists valid words and the connects each to the applicable rules

For a more detailed description of the Hunspell file format check
http://pwet.fr/man/linux/fichiers_speciaux/hunspell

2 The .aff file

In the metadata section, encoding is set to UTF-8 and line IDs are defined as numbers. Although
most rule IDs in Norsk ordbank are three digit numbers, some also contain letters. The rule ID in
the .aff file is a sequence number in which the rule is encountered.

All rules are SFX rules, and neither of them is allowed to be combined with other rules.

Replacement rules are calculated from the patterns in Norsk ordbank. Duplications are filtered,
so the Hunspell rule may contain fewer lines than the corresponding paradigm in Norsk ordbank.
Replacement condition is '.' in all cases.

A simple example, for 'm1', e.g. 'bil':

```
SFX 374 N 3
SFX 374 0 en .
SFX 374 0 ar .
SFX 374 0 ane .
```

Empty patterns are replaced by '0'. Placeholders '+' and '%' are expanded by going through all
lemma to which the rule applies. All possible values are extracted, and all possible combinations
of the placeholders are written as separate replacements.

An example with 3 possible replacements for '+':

```
SFX 381 N 9
SFX 381 el elen .
SFX 381 el lar .
SFX 381 el lane .
SFX 381 er eren .
SFX 381 er rar .
SFX 381 er rane .
SFX 381 en enen .
SFX 381 en nar .
SFX 381 en nane .
```

3 The .dic file format

The first line is the number of words. Subsequent lines contain the lemma, optionally followed by
a slash and a comma-separated list of rules.

The .aff file generation populates a hash which key-value pairs match the Ordbank rule IDs to the
ID used in the .aff file. When creating the .dic file, the following extra rules are used:

* Words containing slashes (e.g. A/S) must be surrounded by double quotes.

* If a word has no inflected forms it stands in its own line, without a slash or any rule ID. Some
  applications however fail to recognize words unless at least one rule is defined for them. A
  special dummy rule that only allows an unlikely inflected form is attached to the end of the
  .aff file, and is defined for such words.

* Some applications fail to recognize inflected forms if the whole word should be replaced, i.e.
  if the search pattern is the same as the lemma. To circumvent this, each inflected form is added
  in these cases.
