import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 1_000_000; i++) {
            numbers.add(i);
        }

        // Ortak ArrayList'ler oluşturuluyor
        List<Integer> evenNumbers = new ArrayList<>();
        List<Integer> oddNumbers = new ArrayList<>();
        List<Integer> primeNumbers = new ArrayList<>();

        // Thread'ler oluşturuluyor
        Thread evenThread = new Thread(new NumberFinder(numbers, evenNumbers, NumberType.EVEN));
        Thread oddThread = new Thread(new NumberFinder(numbers, oddNumbers, NumberType.ODD));
        Thread primeThread = new Thread(new NumberFinder(numbers, primeNumbers, NumberType.PRIME));

        // Thread'ler başlatılıyor
        evenThread.start();
        oddThread.start();
        primeThread.start();

        // Thread'lerin tamamlanmasını bekliyoruz
        evenThread.join();
        oddThread.join();
        primeThread.join();

        // Sonuçları yazdırma
        System.out.println("Even Numbers: " + evenNumbers);
        System.out.println("Odd Numbers: " + oddNumbers);
        System.out.println("Prime Numbers: " + primeNumbers);
    }
}

// Sayı türünü belirten enum
enum NumberType {
    EVEN, ODD, PRIME
}

class NumberFinder implements Runnable {
    private final List<Integer> numbers;
    private final List<Integer> resultNumbers;
    private final NumberType type;

    public NumberFinder(List<Integer> numbers, List<Integer> resultNumbers, NumberType type) {
        this.numbers = numbers;
        this.resultNumbers = resultNumbers;
        this.type = type;
    }

    @Override
    public void run() {
        for (int num : numbers) {
            switch (type) {
                case EVEN:
                    if (num % 2 == 0) {
                        synchronized (resultNumbers) {
                            resultNumbers.add(num);
                        }
                    }
                    break;
                case ODD:
                    if (num % 2 != 0) {
                        synchronized (resultNumbers) {
                            resultNumbers.add(num);
                        }
                    }
                    break;
                case PRIME:
                    if (isPrime(num)) {
                        synchronized (resultNumbers) {
                            resultNumbers.add(num);
                        }
                    }
                    break;
            }
        }
    }

    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
