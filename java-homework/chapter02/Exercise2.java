public class Exercise2 
{
    public static void main(String[] args)
    {
        int op1=(int)(Math.random() * 10);
        int op2=(int)(Math.random() * 9 + 1);
        int rm=(int)((Math.random()) * 1000) % 4;
        double result = 0;
        char[] opstr={'+','-','*','/'};
        switch (rm) 
            { 
            case 0: 
                result = op1 + op2;
                break;
            case 1: 
                result = op1 - op2;
                break;
            case 2:
                result = op1 * op2;
                break;
            case 3:
                result = op1*1.0 / op2;
                break;
            }
        System.out.print(op1);
        System.out.print(opstr[rm]);
        System.out.print(op2 + "=" + result);
    }
}
