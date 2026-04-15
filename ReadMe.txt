Elliptic-curve-Diffie-Hellman

	Démonstrateur de l'utilisation de Diffie-Hellman avec des courbes elliptiques
Ce démonstrateur est codé en Java et à pour rendu graphique Swing.

1) - Pour commencer

Si vous souhaitez lancer directement le démonstrateur vous pouvez dans un terminal effectuer la commande $ ant

elliptic-curve-diffie-hellman $ ant

En lançant cette commande le buid.xml va effectuer la compilation, la javadoc puis exécuter et lancer  le démonstrateur.

Si vous souhaitez générer seulement la javadoc : elliptic-curve-diffie-hellman $ ant javadoc

Si vous souhaitez seulement compiler le code : elliptic-curve-diffie-hellman $ ant compile

Si vous souhaitez seulement éxécuter le code après avoir compiler : elliptic-curve-diffie-hellman $ ant run

Si vous souhaitez supprimer la javadoc et  le fichier de build : elliptic-curve-diffie-hellman $ ant clean

Une fois la commande ant lancée ou la compilation et éxécution le démonstrateur se lance.


2) - Démonstrateur 

Le démonstrateur est  constitué de deux types de simulations :

1 - Le mode pédagogique : Il utilise des  nombres p,g a et b de petites tailles pour pouvoir démontrer les calculs.
2 - Le mode  réaliste : Il utilise  des nombres p,a et  b de très grandes tailles (≃ 78 bits en moyenne). Ce sont des nombres premiers sûrs.


Chaque simulation peut se dérouler de 3 façons différentes : 

	1 - Mode étape par étape : Il permet sans contraintes de temps de suivre ce qui passe à chacune des 9 étapes du démonstrateur. Le démonstrateur passe à l'étape  suivante à chaque appui sur le  bouton.
	
	2 - Mode  de démo complet : Il permet  d'effectuer instantanément les  9 étapes.
	
	3 - Mode automatique  : Il permet comme  son nom l'indique d'afficher les étapes de façon automatique, il laisse quelques secondes (≃5 secondes) entre  chaque étape pour laisser une certaine visibilité du déroulement du démonstrateur.

Chaque étape de l'échange et le détail des calculs réalisés est aussi visible dans le démonstrateur. La partie de détail de calcul peut-être remontée.

En plus de la partie  Diffie-Hellman il est possible de sélectionner cette partie sur courbe elliptique.
Dans cette partie il est visible une courbe  elliptique avec un exemple d'utilisation ainsi que l'explication d'une courbe elliptique avec Diffie-Hellman.


Auteurs : 
LE BASNIER Audrey
THOMAS Matthieu
TELLIER  Basile
