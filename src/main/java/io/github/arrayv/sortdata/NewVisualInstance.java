package io.github.arrayv.sortdata;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.visuals.Visual;

public final class NewVisualInstance implements Supplier<Visual> {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.publicLookup();
    private static final MethodType CONSTRUCTOR_TYPE = MethodType.methodType(void.class, ArrayVisualizer.class);

    private final MethodHandle mh;

    public NewVisualInstance(MethodHandle mh) {
        this.mh = mh;
    }

    public NewVisualInstance(Constructor<? extends Visual> constructor) throws IllegalAccessException {
        this(LOOKUP.unreflectConstructor(constructor));
    }

    public NewVisualInstance(Class<? extends Visual> vslClass) throws NoSuchMethodException, IllegalAccessException {
        this(getMh(vslClass));
    }

    public NewVisualInstance(Visual visual) throws IllegalAccessException, NoSuchMethodException {
        this(visual.getClass());
    }

    private static MethodHandle getMh(Class<? extends Visual> vslClass) throws NoSuchMethodException, IllegalAccessException {
        return LOOKUP.findConstructor(vslClass, CONSTRUCTOR_TYPE);
    }

    public MethodHandle getConstructorHandle() {
        return mh;
    }

    @Override
    public Visual get() {
        try {
            return (Visual)mh.invoke(ArrayVisualizer.getInstance());
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
