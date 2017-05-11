package CIM;

/**
 * <code>ComplexNumber</code> is a class which implements complex numbers in Java. 
 * It includes basic operations that can be performed on complex numbers such as,
 * addition, subtraction, multiplication, conjugate, modulus and squaring. 
 * The data type for Complex Numbers.
 * <br /><br />
 * The features of this library include:<br />
 * <ul>
 * <li>Arithmetic Operations (addition, subtraction, multiplication, division)</li>
 * <li>Complex Specific Operations - Conjugate, Inverse, Absolute/Magnitude, Argument/Phase</li>
 * <li>Trigonometric Operations - sin, cos, tan, cot, sec, cosec</li>
 * <li>Mathematical Functions - exp</li>
 * <li>Complex Parsing of type x+yi</li>
 * </ul>
 * 
 * @author      Abdul Fatir
 * @version     1.1
 * 
 */
public class ComplexNumber {
    /**
    * Used in <code>format(int)</code> to format the complex number as x+yi
    */
    public static final int XY = 0;
    /**
    * Used in <code>format(int)</code> to format the complex number as R.cis(theta), where theta is arg(z)
    */
    public static final int RCIS = 1;
    /**
    * The real, Re(z), part of the <code>ComplexNumber</code>.
    */
    private double real;
    /**
    * The imaginary, Im(z), part of the <code>ComplexNumber</code>.
    */
    private double imaginary;
    /**
    * Constructs a new <code>ComplexNumber</code> object with both real and imaginary parts 0 (z = 0 + 0i).
    */
    public ComplexNumber()
    {
        real = 0.0;
        imaginary = 0.0;
    }

    /**
    * Constructs a new <code>ComplexNumber</code> object.
    * @param real the real part, Re(z), of the complex number
    * @param imaginary the imaginary part, Im(z), of the complex number
    */

    public ComplexNumber(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImaginary() {
		return imaginary;
	}

	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}

	/**
    * Adds another <code>ComplexNumber</code> to the current complex number.
    * @param z the complex number to be added to the current complex number
    */

    public void add(ComplexNumber z)
    {
        set(add(this,z));
    }

    /**
    * Subtracts another <code>ComplexNumber</code> from the current complex number.
    * @param z the complex number to be subtracted from the current complex number
    */

    public void subtract(ComplexNumber z)
    {
        set(subtract(this,z));
    }

    /**
    * Multiplies another <code>ComplexNumber</code> to the current complex number.
    * @param z the complex number to be multiplied to the current complex number
    */

    public void multiply(ComplexNumber z)
    {
        set(multiply(this,z));
    }
    /**
    * Divides the current <code>ComplexNumber</code> by another <code>ComplexNumber</code>.
    * @param z the divisor
    */  
    public void divide(ComplexNumber z)
    {
        set(divide(this,z));
    }
    /**
    * Sets the value of current complex number to the passed complex number.
    * @param z the complex number
    */
    public void set(ComplexNumber z)
    {
        this.real = z.real;
        this.imaginary = z.imaginary;
    }
    /**
    * Adds two <code>ComplexNumber</code>.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 + z2).
    */
    public static ComplexNumber add(ComplexNumber z1, ComplexNumber z2)
    {
        return new ComplexNumber(z1.real + z2.real, z1.imaginary + z2.imaginary);
    }
    
    public void addTo(ComplexNumber z1){
    	this.real+=z1.real;
    	this.imaginary+=z1.imaginary;
    }
    /**
    * Subtracts one <code>ComplexNumber</code> from another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 - z2).
    */  
    public static ComplexNumber subtract(ComplexNumber z1, ComplexNumber z2)
    {
        return new ComplexNumber(z1.real - z2.real, z1.imaginary - z2.imaginary);
    }
    /**
    * Multiplies one <code>ComplexNumber</code> to another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 * z2).
    */  
    public static ComplexNumber multiply(ComplexNumber z1, ComplexNumber z2)
    {
        double _real = z1.real*z2.real - z1.imaginary*z2.imaginary;
        double _imaginary = z1.real*z2.imaginary + z1.imaginary*z2.real;
        return new ComplexNumber(_real,_imaginary);
    }
    /**
    * Divides one <code>ComplexNumber</code> by another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 / z2).
    */      
    public static ComplexNumber divide(ComplexNumber z1, ComplexNumber z2)
    {
        ComplexNumber output = multiply(z1,z2.conjugate());
        double div = Math.pow(z2.mod(),2);
        return new ComplexNumber(output.real/div,output.imaginary/div);
    }
    
    public void inv()
    {	
    	ComplexNumber one = new ComplexNumber(1,0);	
        ComplexNumber output = multiply(one,this.conjugate());
        double div = Math.pow(this.mod(),2);
        this.real=output.real/div;
        this.imaginary=output.imaginary/div;
    }
    public void neg()
    {	
    	ComplexNumber minus = new ComplexNumber(-1,0);	
        this.real = multiply(minus,this).real;
        this.imaginary = multiply(minus,this).imaginary;       
    }
    /**
    * The complex conjugate of the current complex number.
    * @return a <code>ComplexNumber</code> object which is the conjugate of the current complex number
    */

    public ComplexNumber conjugate()
    {
        return new ComplexNumber(this.real,-this.imaginary);
    }

    /**
    * The modulus, magnitude or the absolute value of current complex number.
    * @return the magnitude or modulus of current complex number
    */

    public double mod()
    {
        return Math.sqrt(Math.pow(this.real,2) + Math.pow(this.imaginary,2));
    }

   
    /**
    * @return the complex number in x + yi format
    */
    @Override
    public String toString()
    {
    	double real=Math.round(this.real*1000.0)/1000.0;
    	double imaginary=Math.round(this.imaginary*1000.0)/1000.0;
        String re = real+"";
        String im = "";
        if(imaginary < 0)
            im = imaginary+"i";
        else
            im = "+"+imaginary+"i";
        return re+im;
    }
    /**
    * Calculates the <code>ComplexNumber</code> to the passed integer power.
    * @param z The input complex number
    * @param power The power.
    * @return a <code>ComplexNumber</code> which is (z)^power
    */
    public static ComplexNumber pow(ComplexNumber z, int power)
    {
        ComplexNumber output = new ComplexNumber(z.getRe(),z.getIm());
        for(int i = 1; i < power; i++)
        {
            double _real = output.real*z.real - output.imaginary*z.imaginary;
            double _imaginary = output.real*z.imaginary + output.imaginary*z.real;
            output = new ComplexNumber(_real,_imaginary);
        }
        return output;
    }
    
    /**
    * The real part of <code>ComplexNumber</code>
    * @return the real part of the complex number
    */
    public double getRe()
    {
        return this.real;
    }
    /**
    * The imaginary part of <code>ComplexNumber</code>
    * @return the imaginary part of the complex number
    */
    public double getIm()
    {
        return this.imaginary;
    }
    /**
    * The argument/phase of the current complex number.
    * @return arg(z) - the argument of current complex number
    */
    public double getArg()
    {
        return Math.atan2(imaginary,real);
    }
    
   
}