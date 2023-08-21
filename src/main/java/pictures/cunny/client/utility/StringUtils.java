package pictures.cunny.client.utility;

import it.unimi.dsi.fastutil.objects.ObjectBigArrayBigList;
import lombok.SneakyThrows;
import pictures.cunny.client.Cunny;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class StringUtils {
    public static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final List<String> alphabet = Arrays.stream("qwertyuiopasdfghjklzxcvbnm1234567890/._-".split("")).toList();
    private static final ObjectBigArrayBigList<String> wordList = new ObjectBigArrayBigList<>();
    private static final Map<String, String> owoMap = new HashMap<>();
    private static final Path WORDS = PathIndex.CUNNY.resolve("words.txt");
    private static Long worldListStart = 0L;

    public StringUtils() {
        Cunny.EXECUTOR.submit(StringUtils::loadWordList);
        owoMap.putAll(Map.of("hacker", "haxor", "hacks", "hax",
                "ch", "chw", "Qu", "Qwu", "qu", "qwu"));
        owoMap.putAll(Map.of("r", "w", "l", "w", "R", "W",
                "L", "W", "no", "nu", "has", "haz",
                "have", "haz", "you", "uu", "the ", "da ", "The ", "Da "));
    }

    public static String readable(String original) {
        original = original.toLowerCase();
        StringBuilder newString = new StringBuilder();
        if (original.contains("-") || original.contains("_")) {
            for (String segment : original.split("[-_]")) {
                String first = segment.split("")[0];
                newString.append(segment.replaceFirst(first, first.toUpperCase(Locale.ROOT)));
            }
        } else {
            newString = new StringBuilder(original.toLowerCase(Locale.ROOT));
            String first = newString.toString().split("")[0];
            newString = new StringBuilder(newString.toString().replaceFirst(first, first.toUpperCase(Locale.ROOT)));
        }

        return newString.toString();
    }

    public static String owoify(String str) {
        var ref = new Object() {
            String ph = str;
        };
        owoMap.forEach((s, s2) -> {
            if (ref.ph.contains(s)) ref.ph = ref.ph.replaceAll(s, s2);
        });
        return ref.ph;
    }

    public static boolean toBool(String string) {
        return switch (string) {
            case "true", "yes", "1", "y" -> true;
            default -> false;
        };
    }

    @SneakyThrows
    public static void loadWordList() {
        if (worldListStart == 0) worldListStart = System.nanoTime();

        if (Files.exists(WORDS) && Files.isRegularFile(WORDS)) {
            wordList.addAll(Files.readAllLines(WORDS));
            Cunny.LOG.info("Word List took {}ns to load", System.nanoTime() - worldListStart);
        } else {
            Files.write(WORDS,
                    FileSystem.getUrl("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt")
                            .getBytes(),
                    StandardOpenOption.CREATE_NEW);
            loadWordList();
        }
    }

    public static String randomText(int amount) {
        return randomText(amount, false);
    }

    public static String randomText(int amount, boolean uni) {
        StringBuilder str = new StringBuilder();

        if (uni) {
            int leftLimit = 123;
            int rightLimit = 20000;

            for (int i = 0; i < amount; i++) {
                str.append((char) (leftLimit + (int) (RANDOM.nextFloat() * (rightLimit - leftLimit + 1))));
            }
        } else {
            for (int i = 0; i < amount; i++) {
                str.append(CollectionUtils.random(alphabet));
            }
        }

        return str.toString();
    }

    public static String randomInt(int amount) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            str.append(RANDOM.nextInt(9));
        }
        return str.toString();
    }

    public String randomWord() {
        String random;

        do {
            random = CollectionUtils.random(wordList);
        } while (random == null || random.isBlank());

        return random.strip();
    }

    public String randomWord(Predicate<String> filter) {
        String random;

        do {
            random = CollectionUtils.random(wordList);
        } while (random == null || random.isBlank() || !filter.test(random));

        return random.strip();
    }
}
