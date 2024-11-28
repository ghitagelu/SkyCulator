package com.example.SkyCulator.FreeFallCalculations

class FreeFallCalculator {

    val mass1 = 105.0 // Mass of person 1 in kilograms
    val mass2 = 95.0 // Mass of person 2 in kilograms
    val height1 = 1.85 // Height of person 1 in meters
    val height2 = 1.75 // Height of person 2 in meters
    val clothingDragCoefficient1 = 1.2 // Coefficient for clothing drag for person 1
    val clothingDragCoefficient2 = 0.9 // Coefficient for clothing drag for person 2

    val accelerationDifference = calculateAcceleration(height1, height2, mass1, mass2, clothingDragCoefficient1, clothingDragCoefficient2)

//    println("Difference in acceleration between person 1 and person 2: $accelerationDifference m/s²")







//    println("Difference in acceleration between person 1 and person 2: $accelerationDifference m/s²")

//    Tight-Fitting Athletic Clothing (e.g., Spandex, Lycra): 0.1
//    Regular Casual Clothing (e.g., Jeans and T-Shirt):0.7
//    Wingsuit: 1 - 1.5
//    Winter Coat and Heavier Clothing: 1 - 1.2

}
fun calculateAcceleration(height1: Double, height2: Double, mass1: Double, mass2: Double, clothingDragCoefficient1: Double, clothingDragCoefficient2: Double): Double {
    // Constants for the calculation
    val gravitationalAcceleration = 9.81 // Gravitational acceleration on Earth in m/s^2

    // Calculate the gravitational forces for each person
    val gravitationalForce1 = mass1 * gravitationalAcceleration
    val gravitationalForce2 = mass2 * gravitationalAcceleration

    // Calculate the drag forces for each person using the modified calculateDragForce function
    val dragForce1 = calculateDragForce(mass1, height1, clothingDragCoefficient1)
    val dragForce2 = calculateDragForce(mass2, height2, clothingDragCoefficient2)

    // Calculate the net forces for each person (gravitational force - drag force)
    val netForce1 = gravitationalForce1 - dragForce1
    val netForce2 = gravitationalForce2 - dragForce2

    // Calculate the accelerations for each person using Newton's second law (F = ma)
    val acceleration1 = netForce1 / mass1
    val acceleration2 = netForce2 / mass2

    return acceleration1 - acceleration2 // Difference in acceleration between person 1 and person 2
}

fun calculateDragForce(mass: Double, height: Double, clothingDragCoefficient: Double): Double {
    // Constants for the calculation
    val airDensity = 1.225 // Air density at sea level and 15°C in kg/m³
    val freeFallVelocity = 53.0 // Estimated free-fall velocity in m/s

    // Calculate reference area based on height (assuming a cylindrical shape)
    val referenceArea = Math.PI * (height / 2.0) * (height / 2.0)

    // Calculate drag force with the additional clothing drag coefficient
    val dragForce = 0.5 * (1.0 + clothingDragCoefficient) * airDensity * referenceArea * freeFallVelocity * freeFallVelocity

    // Convert the force to Newtons (since 1 N = 1 kg*m/s^2)
    val forceInNewtons = dragForce * mass

    return forceInNewtons
}
