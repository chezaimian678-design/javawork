import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;
public class KMeans
{
    public static void main(String[] args) throws IOException
    {
        int n = 20;
        int k = 3;
        Random random = new Random(1234L);
        double[][] data = new double[n][2];
        double[][] centers = new double[k][2];
        int[] group = new int[n];
        for (int i = 0; i < n; i++)
        {
            data[i][0] = random.nextDouble() * 100;
            data[i][1] = random.nextDouble() * 100;
        }
        for (int i = 0; i < k; i++)
        {
            centers[i] = data[random.nextInt(n)].clone();
        }
        while (true)
        {
            for (int i = 0; i < n; i++)
            {
                double minDistance = Double.MAX_VALUE;
                for (int j = 0; j < k; j++)
                {
                    double x = data[i][0] - centers[j][0];
                    double y = data[i][1] - centers[j][1];
                    double distance = x * x + y * y;
                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        group[i] = j;
                    }
                }
            
            }
        double[][] sum = new double[k][2];
        int[] count = new int[k];
        for (int i = 0; i < n; i++)
        {
            int g = group[i];
            sum[g][0] += data[i][0];
            sum[g][1] += data[i][1];
            count[g]++;
        }
        boolean changed = false;
        for (int j = 0; j < k; j++)
        {
            if (count[j] == 0)
            {
                continue;
            }
            double newX = sum[j][0] / count[j];
            double newY = sum[j][1] / count[j];
            if (newX != centers[j][0] || newY != centers[j][1])
            {
                changed = true;
            }
            centers[j][0] = newX;
            centers[j][1] = newY;

        }
        if (!changed)
        {
            break;
        }
        
    }
    PrintWriter writer = new PrintWriter("result.txt");
    for (int i = 0; i < n; i++)
    {
        writer.println(
            data[i][0] + " "
            + data[i][1] + " "
            + group[i]);
    }
    writer.close();
    }
}
