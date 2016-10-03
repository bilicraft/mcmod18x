package ruby.bamboo.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

public class NBTTagListEx<E extends NBTBase> extends NBTTagList implements Collection<E> {
    private static final Logger LOGGER = LogManager.getLogger();
    private List<NBTBase> tagList = Lists.newArrayList();

    public NBTTagListEx(NBTTagList parent) {
        for (Field f : NBTTagList.class.getDeclaredFields()) {
            if (List.class.isInstance(f.getType())) {
                f.setAccessible(true);
                try {
                    tagList = (List<NBTBase>) f.get(parent);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int size() {
        return tagList.size();
    }

    @Override
    public boolean isEmpty() {
        return tagList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return tagList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>) tagList.iterator();
    }

    @Override
    public Object[] toArray() {
        return tagList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return tagList.toArray(a);
    }

    @Override
    public boolean add(E e) {
        int size = tagList.size();
        this.appendTag(e);
        return tagList.size() != size;
    }

    @Override
    public boolean remove(Object o) {
        return tagList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return tagList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        List clone = Lists.newArrayList(tagList);
        for (E e : c) {
            if (!clone.add(e)) {
                return false;
            }
        }
        tagList = clone;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return tagList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return tagList.retainAll(c);
    }

    @Override
    public void clear() {
        tagList.clear();
    }

}
