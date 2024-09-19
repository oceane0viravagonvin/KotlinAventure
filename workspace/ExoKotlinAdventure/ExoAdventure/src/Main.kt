import kotlin.random.Random

fun lanceDes(nombreDes: Int, nombreFaces: Int): Int {
    var resultatTotal = 0

    // Pour chaque dé, on génère un nombre aléatoire entre 1 et le nombre de faces
    for (i in 1..nombreDes) {
        val resultatDe = Random.nextInt(1, nombreFaces + 1)
        resultatTotal += resultatDe
        println("Résultat du dé $i : $resultatDe")
    }

    return resultatTotal
}


//Calcul de la défense totale
fun calculDefenseTotal(defense: Int, bonusTypeArmure: Int, bonusQualite: Int): Int = defense + bonusTypeArmure + bonusQualite

// Calcul des dégâts d'une arme
fun calculDegatArme(nbDes: Int, nbFaces: Int, bonusQualite: Int, seuilCritique: Int, multiplicateurCritique: Int): Int {
    val degats = lanceDes(nbDes, nbFaces)
    val finalDegats = if (degats >= seuilCritique) degats * multiplicateurCritique else degats
    return maxOf(0, finalDegats + bonusQualite)
}

// Simuler une attaque
fun attaque(pvCible: Int, defenseCible: Int, degatsArme: Int, nomAttaquant: String, nomCible: String) {
    val degats = maxOf(0, degatsArme - defenseCible)
    val pvRestants = maxOf(0, pvCible - degats)
    println("$nomAttaquant attaque $nomCible pour $degats points de dégâts. $pvRestants PV restants.")
}

// Boire une potion
fun boirePotion(nomCible: String, pv: Int, pvMax: Int, puissancePotion: Int) {
    val exPV = pv
    val nouveauPV = minOf(pv + puissancePotion, pvMax)
    val pvRecuperes = nouveauPV - exPV
    println("$nomCible boit une potion et récupère $pvRecuperes PV, $nouveauPV PV restants.")
}

// Lancer une boule de feu
fun bouleDeFeu(nomCaster: String, nomCible: String, attaqueCaster: Int, defenseCible: Int, pvCible: Int) {
    val degats = maxOf(0, lanceDes(attaqueCaster / 3, 6) - defenseCible)
    val pvRestants = maxOf(0, pvCible - degats)
    println("$nomCaster lance une boule de feu et inflige $degats points de dégâts à $nomCible. $pvRestants PV restants.")
}

// Lancer un missile magique
fun missileMagique(nomCaster: String, nomCible: String, attaqueCaster: Int, defenseCible: Int, pvCible: Int) {
    var pvRestants = pvCible
    val nbMissiles = attaqueCaster / 2
    for (i in 1..nbMissiles) {
        val degats = maxOf(0, lanceDes(1, 6) - defenseCible)
        pvRestants = maxOf(0, pvRestants - degats)
        println("$nomCaster lance un missile magique et inflige $degats points de dégâts à $nomCible. $pvRestants PV restants.")
        if (pvRestants == 0) break
    }
}

// Invoquer une arme
fun invocationArme(nomCaster: String, typeArme: String) {
    val qualite = when (Random.nextInt(1, 21)) {
        in 1..4 -> "commune"
        in 5..9 -> "rare"
        in 10..14 -> "épique"
        else -> "légendaire"
    }
    println("$nomCaster invoque une $typeArme $qualite.")
}

// Lancer un sort de soins
fun soins(nomCaster: String, attaqueCaster: Int, pv: Int, pvMax: Int, estMortVivant: Boolean) {
    val scoreSoin = attaqueCaster / 2
    val pvRestants = if (estMortVivant) maxOf(0, pv - scoreSoin) else minOf(pv + scoreSoin, pvMax)
    val action = if (estMortVivant) "inflige $scoreSoin dégâts" else "soigne $scoreSoin PV"
    println("$nomCaster $action à la cible. $pvRestants PV restants.")
}

// Afficher l'inventaire
fun afficheInventaire(nomPerso: String, inventaire: MutableList<String>) {
    println("Inventaire de $nomPerso:")
    inventaire.forEachIndexed { index, item -> println("${index + 1} => $item") }
}

// Choisir un item
fun choisirItem(nomPerso: String, inventaire: MutableList<String>) {
    afficheInventaire(nomPerso, inventaire)
    print("Choisissez un item: ")
    val choix = readLine()?.toIntOrNull() ?: -1
    if (choix in 1..inventaire.size) {
        println("$nomPerso utilise ${inventaire[choix - 1]}")
    } else {
        println("Choix invalide, veuillez réessayer.")
        choisirItem(nomPerso, inventaire)
    }
}

// Tour du joueur
fun tourJoueur() {
    println("Choisissez une action:")
    println("1 => Attaquer\n2 => Boule de feu\n3 => Missile magique\n4 => Soins\n5 => Utiliser un item")
    when (readLine()) {
        "1" -> println("Action: Attaquer")
        "2" -> println("Action: Boule de feu")
        "3" -> println("Action: Missile magique")
        "4" -> println("Action: Soins")
        "5" -> println("Action: Utiliser un item")
        else -> {
            println("Choix invalide, réessayez.")
            tourJoueur()
        }
    }
}

// Tour de l'ordinateur
fun tourOrdinateur() {
    when (Random.nextInt(1, 31)) {
        in 1..15 -> println("L'ordinateur attaque.")
        in 16..20 -> println("L'ordinateur lance une boule de feu.")
        in 21..25 -> println("L'ordinateur lance un missile magique.")
        else -> println("L'ordinateur lance des soins.")
    }
}

// Tour de combat
fun tourCombat(pvJoueur: Int, pvOrdinateur: Int) {
    var pvJ = pvJoueur
    var pvO = pvOrdinateur
    println("Début du Tour!")

    while (pvJ > 0 && pvO > 0) {
        println("\nTour du joueur:")
        tourJoueur()

        // Simuler une attaque du joueur sur l'ordinateur (par exemple ici avec des dégâts fictifs)
        val degatsJoueur = Random.nextInt(1, 6) // Exemple de dégâts infligés par le joueur
        pvO = maxOf(1, pvO - degatsJoueur) // Réduire les PV de l'ordinateur
        println("L'ordinateur a maintenant $pvO PV.")
        if (pvO == 0) {
            println("L'ordinateur a été vaincu!")
            break
        }

        println("\nTour de l'ordinateur:")
        tourOrdinateur()

        // Simuler une attaque de l'ordinateur sur le joueur (par exemple ici avec des dégâts fictifs)
        val degatsOrdinateur = Random.nextInt(1, 6) // Exemple de dégâts infligés par l'ordinateur
        pvJ = maxOf(0, pvJ - degatsOrdinateur) // Réduire les PV du joueur
        println("Le joueur a maintenant $pvJ PV.")
        if (pvJ == 0) {
            println("Le joueur a été vaincu!")
            break
        }
    }

    println("Fin du Tour.")
}



fun main() {
    // 1d6 (un dé à 6 faces)
    val resultat1d6 = lanceDes(1, 6)
    println("Résultat de 1d6 : $resultat1d6")
    println()

    val inventaire = mutableListOf("Épée", "Bouclier", "Potion de soin")
    choisirItem("Héros", inventaire)

    tourCombat(1, 1)
}
