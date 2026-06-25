namespace Proiect.Utils;

// Contine validari simple pentru campurile din UI.
public static class Validator
{
    // Input: string value.
    // Return: bool.
    // Verifica daca textul are doar litere.
    // Respinge null, gol sau spatii.
    public static bool ContainsOnlyLetters(string value)
    {
        return !string.IsNullOrWhiteSpace(value) && value.All(char.IsLetter);
    }
}