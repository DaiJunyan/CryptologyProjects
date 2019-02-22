/*
MATH3815 project3 --- Diffie-Hellman Key Exchange simulation
                      implementing Fast Exponentiation and Miller-Rabin Primality Test
Designed by Dai Junyan
Email: daiju@kean.edu
Wenzhou-Kean University
 */

import java.util.Scanner;


public class cryptoProject3 {
    //Domain parameters
    static int p;
    static int g;
    public static void main(String[] args) {
        //generate domain parameters
        generate_DP();

        //Alice's private key
        int a = generate_Kpr();
        System.out.println("Alice chooses her private key(randomly): " + a);
        //Bob's private key
        int b = generate_Kpr();
        System.out.println("Bob chooses his private key(randomly): " + b);

        //compute public keys using Square-and-Multiply for Modular Exponentiation
        //Alice's public key
        int A = fast_computation(g,a,p);
        System.out.println("Alice sends to Bob her computed public key: "+ A);
        //Bob's public key
        int B = fast_computation(g,b,p);
        System.out.println("Bob sends to Alice his computed public key: "+ B);

        //compute common key
        int keyA = fast_computation(B,a,p);
        System.out.println("Alice got the common key: "+keyA);
        int keyB = fast_computation(A,b,p);
        System.out.println("Bob got the common key: "+keyB);
    }
    private static void generate_DP(){
        Scanner input=new Scanner(System.in);
        //choose a prime p
        boolean while_loop=true;
        while(while_loop) {
            System.out.print("Enter a prime p: ");
            p = input.nextInt();
            if (is_prime(p))
                while_loop = false;
            else
                System.out.println("ERROR! The number " + p + " is not a prime number!");
        }

        //choose an integer g random(2,p-2)
        g = 2 + (int)(Math.random()*(p-3));
        System.out.println("g is "+g);
    }
    private static int generate_Kpr(){
      int Kpr = 2 + (int)(Math.random()*(p-3));
      return Kpr;
    }

    private static int fast_computation(int x, int Kpr, int n){
        if(Kpr == 1)
            return x;
        int r = x;
        String h = Integer.toBinaryString(Kpr);
        for(int i = 1;i<h.length();i++){
            r = (int)Math.pow(r,2)%n;
            if(h.charAt(i)=='1')
                r = (r*x)%n;
        }
        return r;
    }

    private static boolean is_prime(int p){
        if (p < 2 || (p != 2 && p % 2==0))
            return false;
        if(p==2)
            return true;

        //obtain r from p-1 = 2^u*r
        int r = (p-1)/2, u = 1;
        while(r%2==0){
            r = r/2;
            u++;
        }
        //security parameter s
        for(int s=0;s<5;s++){
            int a = 2+(int)(Math.random()*(p-3));

            int z = fast_computation(a,r,p);
            if(z!=1&z!=p-1){
                for(int j=1;j<u;j++){
                    z = (int)Math.pow(z,2)%p;
                    if(z==1)
                        return false;
                }
                if(z!=p-1)
                    return false;
            }
        }
        return true;
    }
}
