package com.example.objectpool;

import org.junit.Assert;
import org.junit.Test;

public class ObjectPoolImplTest {

    @Test
    public void testObjectPoolWithInvalidMinSize() {
        final int minSize = -1;
        final int maxSize = 2;
        final boolean autoInit = true;
        try {
            new StringPool(minSize, maxSize, autoInit);
            Assert.fail("Exception should be thrown.");
        } catch (final IllegalArgumentException ex) {
            Assert.assertEquals("Pool minimum size cannot be lower than 0", ex.getMessage());
        }
    }
    @Test
    public void testObjectPoolWithInvalidMaxSize() {
        final int minSize = 4;
        final int maxSize = -2;
        final boolean autoInit = false;
        try {
            new StringPool(minSize, maxSize, autoInit);
             Assert.fail("Exception should be thrown.");
        } catch (final IllegalArgumentException ex) {
            Assert.assertEquals("Pool maximum size cannot be lower than 0", ex.getMessage());
        }
    }
    @Test
    public void testObjectPoolMinGreaterMaxSize() {
        final int minSize = 6;
        final int maxSize = 4;
        final boolean autoInit = false;
        try {
            new StringPool(minSize, maxSize, autoInit);
             Assert.fail("Exception should be thrown.");
        } catch (final IllegalArgumentException ex) {
            Assert.assertEquals("Pool minimum size cannot be greater than maximum size", ex.getMessage());
        }
    }
    @Test
    public void testSingleObjectPool() throws InterruptedException {
        final int minSize = 1;
        final int maxSize = 2;
        final boolean autoInit = false;

        final StringPool stringPool = new StringPool(minSize, maxSize, autoInit);

        final Thread thread1 = new Thread(stringPool::create);

        final Thread thread2 = new Thread(stringPool::create);


        thread1.start();
        thread2.start();


        Thread.sleep(5000);
    }
}