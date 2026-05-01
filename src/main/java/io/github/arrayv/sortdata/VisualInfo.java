package io.github.arrayv.sortdata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Supplier;

import io.github.arrayv.visuals.Visual;

public final class VisualInfo {
    private final int id;
    private final Class<? extends Visual> vslClass;
    private final Supplier<? extends Visual> instanceSupplier;
    private final boolean disabled;
    private final String listName;
    private final String category;
    private final Visual.stance colorability;
    private final boolean auxability;
    // ignoring max aux lists; we only need to know when the list is being rendered
    private final boolean overlayability;

    private VisualInfo(int id, VisualInfo visual) {
        this.id = id;
        this.vslClass = visual.vslClass;
        this.instanceSupplier = visual.instanceSupplier;
        this.disabled = visual.disabled;
        this.listName = visual.listName;
        this.category = visual.category;
        this.colorability = visual.colorability;
        this.auxability = visual.auxability;
        this.overlayability = visual.overlayability;
    }

    public VisualInfo(int id, Class<? extends Visual> vslClass) {
        this.id = id;
        this.vslClass = vslClass;
        try {
            this.instanceSupplier = new NewVisualInstance(vslClass);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Visual visual = getFreshInstance();
        this.disabled = !visual.isVisualEnabled();
        this.listName = visual.getListName();
        this.category = visual.getCategory();
        this.colorability = visual.getColorability();
        this.auxability = visual.isAuxable();
        this.overlayability = visual.isOverlayable();
    }

    public VisualInfo(int id, Visual visual) {
        this.id = id;
        this.vslClass = visual.getClass();
        try {
            this.instanceSupplier = new NewVisualInstance(vslClass);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.disabled = !visual.isVisualEnabled();
        this.listName = visual.getListName();
        this.category = visual.getCategory();
        this.colorability = visual.getColorability();
        this.auxability = visual.isAuxable();
        this.overlayability = visual.isOverlayable();
    }

    public VisualInfo(Visual sort) {
        this(-1, sort);
    }

    public int getId() {
        return id;
    }

    public Supplier<? extends Visual> getInstanceSupplier() {
        return instanceSupplier;
    }

    public String getInternalName() {
        return vslClass != null ? vslClass.getName() : null;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getListName() {
        return listName;
    }

    public String getCategory() {
        return category;
    }

    public Visual.stance getColorability() {
        return colorability;
    }

    public boolean isAuxable() {
        return auxability;
    }

    public boolean isOverlayable() {
        return overlayability;
    }

    public Visual getFreshInstance() {
        return instanceSupplier.get();
    }

    /**
     * Creates a copy of this info with a new ID
     * @param id The ID for the new instance
     * @return Copied info with new ID
     */
    public VisualInfo withId(int id) {
        return new VisualInfo(id, this);
    }

    public static String[] getListNames(VisualInfo[] visuals) {
        String[] result = new String[visuals.length];
        for (int i = 0; i < visuals.length; i++) {
            result[i] = visuals[i].listName;
        }
        return result;
    }

    public static String[] getCategories(VisualInfo[] visuals) {
        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < visuals.length; i++) {
            result.add(visuals[i].category);
        }
        String[] resultArray = result.toArray(new String[result.size()]);
        Arrays.sort(resultArray);
        return resultArray;
    }
}
