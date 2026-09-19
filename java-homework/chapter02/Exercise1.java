import java.util.Scanner;
public class Exercise1
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int money = sc.nextInt();
        int[] faces = {100, 50, 20, 10, 5, 1};
        for(int i = 0 ; i < faces.length ; i++)
        {
            int count = money / faces[i];
            money = money % faces[i];
            System.out.println(faces[i] + "元：" + count + "张");
        }
        sc.close();
    }
}