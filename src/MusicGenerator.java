import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;

/**
 * A procedural generation project that creates piano music in Java.
 * Based off of the work found at: https://gist.github.com/pbloem/d29bf80e69d333415622
 */
public class MusicGenerator {

    // Notes array
    private static List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

    // Major Scales
    private static List<String> C_Major = Arrays.asList("C", "D", "E", "F", "G", "A", "B");
    private static List<String> CSharp_Major = Arrays.asList("C#", "D#", "F", "F#", "G#", "A#", "C");
    private static List<String> D_Major = Arrays.asList("D", "E", "F#", "G", "A", "B", "C#");
    private static List<String> EFlat_Major = Arrays.asList("D#", "F", "G", "G#", "A#", "C", "D");
    private static List<String> E_Major = Arrays.asList("E", "F#", "G#", "A", "B", "C#", "D#");
    private static List<String> F_Major = Arrays.asList("F", "G", "A", "A#", "C", "D", "E");
    private static List<String> FSharp_Major = Arrays.asList("F#", "G#", "A#", "B", "C#", "D#", "E#");
    private static List<String> G_Major = Arrays.asList("G", "A", "B", "C", "D", "E", "F#");
    private static List<String> AFlat_Major = Arrays.asList("G#", "A#", "C", "C#", "D#", "F", "G");
    private static List<String> A_Major = Arrays.asList("A", "B", "C#", "D", "E", "F#", "G#");
    private static List<String> BFlat_Major = Arrays.asList("A#", "C", "D", "D#", "F", "G", "A");
    private static List<String> B_Major = Arrays.asList("B", "C#", "D#", "E", "F#", "G#", "A#");

    // Natural Minor Scales
    private static List<String> C_Minor = Arrays.asList("C", "D", "D#", "F", "G", "G#", "A#");
    private static List<String> D_Minor = Arrays.asList("D", "E", "F", "G", "A", "A#", "C");
    private static List<String> E_Minor = Arrays.asList("E", "F#", "G", "A", "B", "C", "D");
    private static List<String> F_Minor = Arrays.asList("F", "G", "G#", "A#", "C", "C#", "D#");
    private static List<String> G_Minor = Arrays.asList("G", "A", "A#", "C", "D", "D#", "F");
    private static List<String> A_Minor = Arrays.asList("A", "B", "C", "D", "E", "F", "G");
    private static List<String> B_Minor = Arrays.asList("B", "C#", "D", "E", "F#", "G", "A");
    private static List<String> CSharp_Minor = Arrays.asList("C#", "D#", "E", "F#", "G#", "A", "B");
    private static List<String> EFlat_Minor = Arrays.asList("D#", "F", "F#", "G#", "A#", "B#", "C#");
    private static List<String> FSharp_Minor = Arrays.asList("F#", "G#", "A", "B", "C#", "D", "E");
    private static List<String> AFlat_Minor = Arrays.asList("G#", "A#", "B#", "C#", "D#", "E#", "F#");
    private static List<String> BFlat_Minor = Arrays.asList("A#", "C", "C#", "D#", "F", "F#", "G#");

    private static List<String> inputScale;
    private static String inputScaleString;
    private static String inputOctave;

    private static MidiChannel[] channels;
    private static int INSTRUMENT = 0; // 0 is a piano, 9 is percussion
    private static int VOLUME = 80; // between 0 et 127

    public static void main( String[] args ) {

        try {

            // Initialize input
            inputOctave = args[0];
            inputScaleString = args[1];
            int sectionDuration = Integer.parseInt(args[2]);
            int numVerses = Integer.parseInt(args[3]);

            switch (inputScaleString) {
                // Major Scales
                case "CM":
                    inputScale = C_Major;
                    break;
                case "C#M":
                    inputScale = CSharp_Major;
                    break;
                case "DM":
                    inputScale = D_Major;
                    break;
                case "EbM":
                    inputScale = EFlat_Major;
                    break;
                case "EM":
                    inputScale = E_Major;
                    break;
                case "FM":
                    inputScale = F_Major;
                    break;
                case "F#M":
                    inputScale = FSharp_Major;
                    break;
                case "GM":
                    inputScale = G_Major;
                    break;
                case "AbM":
                    inputScale = AFlat_Major;
                    break;
                case "AM":
                    inputScale = A_Major;
                    break;
                case "BbM":
                    inputScale = BFlat_Major;
                    break;
                case "BM":
                    inputScale = B_Major;
                    break;
                // Natural Minor Scales
                case "Cm":
                    inputScale = C_Minor;
                    break;
                case "Dm":
                    inputScale = D_Minor;
                    break;
                case "Em":
                    inputScale = E_Minor;
                    break;
                case "Fm":
                    inputScale = F_Minor;
                    break;
                case "Gm":
                    inputScale = G_Minor;
                    break;
                case "Am":
                    inputScale = A_Minor;
                    break;
                case "Bm":
                    inputScale = B_Minor;
                    break;
                case "C#m":
                    inputScale = CSharp_Minor;
                    break;
                case "Ebm":
                    inputScale = EFlat_Minor;
                    break;
                case "F#m":
                    inputScale = FSharp_Minor;
                    break;
                case "Abm":
                    inputScale = AFlat_Minor;
                    break;
                case "Bbm":
                    inputScale = BFlat_Minor;
                    break;
                // Default C Major Scale
                default:
                    inputScale = C_Major;
            }

            // Open a synthesizer
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();

            // Play the music piece
            playPiece(inputOctave + inputScale.get(0), sectionDuration, numVerses);

            // Finish up
            synth.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Plays the given note for the given duration
     */
    private static void play(String note, double duration) throws InterruptedException {
        // Convert duration to milliseconds
        int d = (int) (duration * 1000);

        // Start playing a note
        channels[INSTRUMENT].noteOn(id(note), VOLUME);

        // Duration of the note
        Thread.sleep(d);

        // Stop playing a note
        channels[INSTRUMENT].noteOff(id(note));
    }

    /**
     * Returns the MIDI id for a given note: eg. 4C -> 60
     * @return correct id
     */
    private static int id(String note) {
        int octave = Integer.parseInt(note.substring(0, 1));
        return notes.indexOf(note.substring(1)) + 12 * octave + 12;
    }

    /**
     * Determines the current duration of the piece and plays the current note
     */
    private static void playPiece(String startingNote, int sectionDuration, int numVerses) throws InterruptedException {
        List<Note> song = generateSong(startingNote, sectionDuration, numVerses);

        // Loop through each note in the array
        for (Note note : song) {
            play(note.getNote(), note.getLength());
        }
    }

    /**
     * Generates the song
     * @return song
     */
    private static List<Note> generateSong(String startingNote, int sectionDuration, int numVerses) {
        List<Note> song = new ArrayList<>();

        // Get random note from appropriate key
        List<Note> intro = generateSection(startingNote, 15);
        song.addAll(intro);

        // Get random note from appropriate key
        List<Note> chorus = generateSection(song.get(song.size() - 1).getNote(), sectionDuration);

        for (int i = 0; i < numVerses; i++) {
            List<Note> verse = generateSection(song.get(song.size() - 1).getNote(), sectionDuration);
            song.addAll(verse);
            song.addAll(chorus);
        }

        List<Note> outro = generateSection(song.get(song.size() - 1).getNote(), 15);
        song.addAll(outro);

        return song;
    }

    /**
     * Generates a section
     * @return section
     */
    private static List<Note> generateSection(String startingNote, int sectionDuration) {
        List<Note> section = new ArrayList<>();
        double noteDurationTotal;

        String nextNote = startingNote;
        double currentNoteDuration = noteDurationTotal = getRandomDouble(0.5, 1.5);
        Note currentNote = new Note(nextNote, currentNoteDuration);
        section.add(currentNote);

        while (noteDurationTotal < sectionDuration) {
            nextNote = generateNextNote(currentNote.getNote());
            currentNoteDuration = getRandomDouble(0.5, 1.5);
            section.add(new Note(nextNote, currentNoteDuration));

            noteDurationTotal = noteDurationTotal + currentNoteDuration;
        }

        return section;
    }

    /**
     * Generates a random double between two numbers
     * @return random double
     */
    private static double getRandomDouble(double min, double max){
        double x = (Math.random()*((max-min)))+min;
        return x;
    }

    /**
     * Generates the next note based on the previous note in the scale
     * @return currentNote
     */
    private static String generateNextNote(String currentNote) {
        double rnd = Math.random();

    // Frequency table mapping the probability of any given note following after the current note
        if (currentNote.equals(inputOctave + inputScale.get(0))) {
            if (rnd <= 0.25)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.25 && rnd <= 0.5)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.5 && rnd <= 0.6)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.6 && rnd <= 0.7)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.7 && rnd <= 0.8)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.8 && rnd <= 0.9)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(1))) {
            if (rnd <= 0.1)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.1 && rnd <= 0.2)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.2 && rnd <= 0.5)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.5 && rnd <= 0.7)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.7 && rnd <= 0.85)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.85 && rnd <= 0.95)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(2))) {
            if (rnd <= 0.25)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.25 && rnd <= 0.4)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.4 && rnd <= 0.65)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.65 && rnd <= 0.7)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.7 && rnd <= 0.825)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.825 && rnd <= 0.95)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(3))) {
            if (rnd <= 0.35)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.35 && rnd <= 0.45)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.45 && rnd <= 0.6)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.6 && rnd <= 0.75)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.75 && rnd <= 0.9)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.9 && rnd <= 0.95)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(4))) {
            if (rnd <= 0.2)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.2 && rnd <= 0.3)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.3 && rnd <= 0.4)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.4 && rnd <= 0.5)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.5 && rnd <= 0.6)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.6 && rnd <= 0.9)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(5))) {
            if (rnd <= 0.25)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.25 && rnd <= 0.4)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.4 && rnd <= 0.5)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.5 && rnd <= 0.7)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.7 && rnd <= 0.825)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.825 && rnd <= 0.9)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(6))) {
            if (rnd <= 0.3)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.3 && rnd <= 0.4)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.4 && rnd <= 0.5)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.5 && rnd <= 0.65)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.65 && rnd <= 0.8)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.8 && rnd <= 0.85)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        } else if (currentNote.equals(inputOctave + inputScale.get(7))) {
            if (rnd <= 0.4)
                currentNote = inputOctave + inputScale.get(0);
            else if (rnd > 0.4 && rnd <= 0.5)
                currentNote = inputOctave + inputScale.get(1);
            else if (rnd > 0.5 && rnd <= 0.6)
                currentNote = inputOctave + inputScale.get(2);
            else if (rnd > 0.6 && rnd <= 0.75)
                currentNote = inputOctave + inputScale.get(3);
            else if (rnd > 0.75 && rnd <= 0.8)
                currentNote = inputOctave + inputScale.get(4);
            else if (rnd > 0.8 && rnd <= 0.9)
                currentNote = inputOctave + inputScale.get(5);
            else
                currentNote = inputOctave + inputScale.get(6);
        }
        return currentNote;
    }
}