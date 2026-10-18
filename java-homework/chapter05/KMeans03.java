import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
interface Distance
{
    double compute(double[] a, double[] b);
}
class EuclideanDistance implements Distance
{
    public double compute(double[] a, double[] b)
    {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++)
        {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
class ManhattanDistance implements Distance
{
    public double compute(double[] a, double[] b)
    {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++)
        {
            sum += Math.abs(a[i] - b[i]);
        }
        return sum;
    }
}
interface Initializer
{
    double[][] init(double[][] data, int k, Random random);
}
class RandomInitializer implements Initializer
{
    public double[][] init(double[][] data, int k, Random random)
    {
        double[][] centers = new double[k][data[0].length];
        for (int i = 0; i < k; i++)
        {
            centers[i] = data[random.nextInt(data.length)].clone();
        }
        return centers;
    }
}

class KMeansPlusPlusInitializer implements Initializer
{
    public double[][] init(double[][] data, int k, Random random)
    {
        double[][] centers = new double[k][data[0].length];
        centers[0] = data[random.nextInt(data.length)].clone();
        for (int c = 1; c < k; c++)
        {
            double[] nearest = new double[data.length];
            double total = 0.0;
            for (int i = 0; i < data.length; i++)
            {
                double min = Double.MAX_VALUE;
                for (int j = 0; j < c; j++)
                {
                    double dist = 0.0;
                    for (int t = 0; t < data[i].length; t++)
                    {
                        double diff = data[i][t] - centers[j][t];
                        dist += diff * diff;
                    }
                    if (dist < min)
                    {
                        min = dist;
                    }
                }
                nearest[i] = min;
                total += min;
            }
            double r = random.nextDouble() * total;
            int pick = 0;
            for (int i = 0; i < data.length; i++)
            {
                r -= nearest[i];
                if (r <= 0.0)
                {
                    pick = i;
                    break;
                }
            }
            centers[c] = data[pick].clone();
        }
        return centers;
    }
}
interface ConvergenceChecker
{
    boolean hasConverged(int[] previous, int[] current);
}
class LabelChangeChecker implements ConvergenceChecker
{
    public boolean hasConverged(int[] previous, int[] current)
    {
        for (int i = 0; i < current.length; i++)
        {
            if (previous[i] != current[i])
            {
                return false;
            }
        }
        return true;
    }
}
class KMeansModel
{
    private final Distance distance;
    private final Initializer initializer;
    private final ConvergenceChecker checker;
    private final int maxIterations;
    private double[][] centroids;
    private int[] labels;

    public KMeansModel(Distance distance, Initializer initializer, ConvergenceChecker checker, int maxIterations)
    {
        this.distance = distance;
        this.initializer = initializer;
        this.checker = checker;
        this.maxIterations = maxIterations;
    }

    public void fit(double[][] data, int k, Random random)
    {
        int n = data.length;
        int d = data[0].length;
        centroids = initializer.init(data, k, random);
        labels = new int[n];
        int[] previous = new int[n];
        for (int i = 0; i < n; i++)
        {
            previous[i] = -1;
        }
        for (int iter = 0; iter < maxIterations; iter++)
        {
            for (int i = 0; i < n; i++)
            {
                double min = Double.MAX_VALUE;
                for (int j = 0; j < k; j++)
                {
                    double dist = distance.compute(data[i], centroids[j]);
                    if (dist < min)
                    {
                        min = dist;
                        labels[i] = j;
                    }
                }
            }
            if (checker.hasConverged(previous, labels))
            {
                break;
            }
            for (int i = 0; i < n; i++)
            {
                previous[i] = labels[i];
            }
            double[][] sum = new double[k][d];
            int[] count = new int[k];
            for (int i = 0; i < n; i++)
            {
                int g = labels[i];
                count[g]++;
                for (int t = 0; t < d; t++)
                {
                    sum[g][t] += data[i][t];
                }
            }
            for (int j = 0; j < k; j++)
            {
                if (count[j] == 0)
                {
                    continue;
                }
                for (int t = 0; t < d; t++)
                {
                    centroids[j][t] = sum[j][t] / count[j];
                }
            }
        }
    }

    public int[] getLabels()
    {
        return labels;
    }

    public double[][] getCentroids()
    {
        return centroids;
    }
}





public class KMeans03 
{
    public static void main(String[] args)
    {
        int n = 20;
        int d = 4;
        int k = 3;
        double[][] data = new double[n][d];
        Random random = new Random(1234L);
        for (int i = 0; i < n; i++)
        {
            for (int t = 0; t < d; t++)
            {
                data[i][t] = random.nextDouble() * 100;
            }
        }
        Distance[] metrics = { new EuclideanDistance(), new ManhattanDistance() };
        Initializer[] initializers = { new RandomInitializer(), new KMeansPlusPlusInitializer() };
        try (PrintWriter out = new PrintWriter(new FileWriter("result.txt")))
        {
            for (Distance metric : metrics)
            {
                for (Initializer init : initializers)
                {
                    KMeansModel model = new KMeansModel(metric, init, new LabelChangeChecker(), 100);
                    model.fit(data, k, new Random(1234L));
                    int[] labels = model.getLabels();
                    String title = "=== " + metric.getClass().getSimpleName() + " + " + init.getClass().getSimpleName() + " ===";
                    out.println(title);
                    System.out.println(title);
                    for (int i = 0; i < n; i++)
                    {
                        StringBuilder line = new StringBuilder();
                        for (int t = 0; t < d; t++)
                        {
                            line.append(data[i][t]).append(' ');
                        }
                        line.append(labels[i]);
                        out.println(line);
                        System.out.println(line);
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Result file: " + new java.io.File("result.txt").getAbsolutePath());
    }
}
