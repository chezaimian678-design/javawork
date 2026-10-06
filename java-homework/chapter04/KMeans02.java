import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;
public class KMeans02
{
    public static void main(String[] args) throws IOException
    {
        int n = 20;
        int k = 3;
        int d = 4;
        int maxIter = 100;
        Random random = new Random(1234L);
        double[][] data = new double[n][d];
        double[][] centers = new double[k][d];
        int[] group = new int[n];
        for (int i = 0; i < n; i++)
        {
            for (int t = 0; t < d; t++)
            {
                data[i][t] = random.nextDouble() * 100;
            }
        }
        for (int i = 0; i < k; i++)
        {
            centers[i] = data[random.nextInt(n)].clone();
        }
        int iter = 0;
        while (iter < maxIter)
        {
            iter++;

            for (int i = 0; i < n; i++)
            {
                double minDistance = Double.MAX_VALUE;
                for (int j = 0; j < k; j++)
                {
                    double distance = 0.0;
                    for (int t = 0; t < d; t++)
                    {
                        double diff = data[i][t] - centers[j][t];
                        distance += diff * diff;
                    }
                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        group[i] = j;
                    }
                }
            }
            double[][] sum = new double[k][d];
            int[] count = new int[k];
            for (int i = 0; i < n; i++)
            {
                int g = group[i];
                for (int t = 0; t < d; t++)
                {
                    sum[g][t] += data[i][t];
                }
                count[g]++;
            }
            boolean changed = false;
            for (int j = 0; j < k; j++)
            {
                if (count[j] == 0)
                {
                    continue;
                }
                for (int t = 0; t < d; t++)
                {
                    double newValue = sum[j][t] / count[j];
                    if (newValue != centers[j][t])
                    {
                        changed = true;
                    }
                    centers[j][t] = newValue;
                }
            }
            if (!changed)
            {
                break;
            }
        }
        PrintWriter writer = new PrintWriter("result.txt");
        for (int i = 0; i < n; i++)
        {
            for (int t = 0; t < d; t++)
            {
                writer.print(data[i][t] + " ");
            }
            writer.println(group[i]);
        }
        writer.close();
        System.out.println("dim=" + d + " iter=" + iter);
    }
}
