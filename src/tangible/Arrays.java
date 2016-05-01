package tangible;

//----------------------------------------------------------------------------------------
//	Copyright © 2006 - 2016 Tangible Software Solutions Inc.
//	This class can be used by anyone provided that the copyright notice remains intact.
//
//	This class provides the ability to initialize array elements with the default
//	constructions for the array type.
//----------------------------------------------------------------------------------------
public final class Arrays {
    public static type[] initializeWithDefaulttypeInstances(int length) {
        type[] array = new type[length];
        for (int i = 0; i < length; i++) {
            array[i] = new type();
        }
        return array;
    }
}