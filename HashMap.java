public class HashMap<K, V> {

    private static final int INIT_BUCKET_COUNT = 16;     // размерность

    private Bucket[] buckets;  // Переменная, содержащая ссылку на массив бакетов.
    class Entity{              // Entity – набор данных состоит из одной строки и формируется                 
        K key;                 // по атрибутам одного экземпляра сущности и связанных с ним сущностей.
        V value;
    }

    class Bucket<K, V>{

        Node head;

        class Node{
            Node next;
            Entity value;

        }

        public V add(Entity entity){            // метод по добавлению элемента
            Node node = new Node();
            node.value = entity;

            if (head == null){
                head = node;
                return null;
            }

            Node currentNode = head;
            while (true){
                if (currentNode.value.key.equals(entity.key)){
                    V buf = (V)currentNode.value.value;
                    currentNode.value.value = entity.value;
                    return buf;
                }
                if (currentNode.next != null)
                    currentNode = currentNode.next;
                else
                {
                    currentNode.next = node;
                    return null;
                }
            }

        }

        public V get(K key){                    // поиск элемента по ключу
            Node node = head;
            while (node != null){
                if (node.value.key.equals(key))
                    return (V)node.value.value;
                node = node.next;
            }
            return null;
        }

        public V remove(K key){                 // метод удаления 
            if (head == null)
                return null;
            if (head.value.key.equals(key)){
                V buf = (V)head.value.value;
                head = head.next;
                return buf;
            }
            else{
                Node node = head;
                while (node.next != null){
                    if (node.next.value.key.equals(key)){
                        V buf = (V)node.next.value.value;
                        node.next = node.next.next;  // удалили промежуточный элемент
                        return buf;
                    }
                    node = node.next;
                }
                return null;
            }
        }

    }

    private int calculateBucketIndex(K key){             // метод определяет индекс, в который пойдет пара элементов
        int index = key.hashCode() % buckets.length;     // наш индекс массива
        index = Math.abs(index);                         // берем значение по модулю
        return index;
    }

    /**
     * Добавить новую пару ключ + значение
     * @param key ключ
     * @param value значение
     * @return предыдущее значение (при совпадении ключа), иначе null
     */
    public V put(K key, V value){
        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null){
            bucket = new Bucket();             // создаем новый объект связного списка
            buckets[index] = bucket;           // помещаем ссылку на этот объект в массив по заданному индексу
        }

        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;
        // именно entity и является элементом узла нашего связного списка
        return (V)bucket.add(entity);
    }

    public V get(K key){                        // поиск элемента по ключу
        int index = calculateBucketIndex(key);  // высчитываем индекс по которому нам нужно обратиться к бакету
        Bucket bucket = buckets[index];
        if (bucket == null)
            return null;
        return (V)bucket.get(key);
    }

    public V remove(K key){                      // метод удаления 
        int index = calculateBucketIndex(key);   // когда мы будем удалять значения по ключу, мы будем 
        Bucket bucket = buckets[index];          // возвращать его значения, а если удалить не получилось
        if (bucket == null)                      // (например, такого ключа нет) , то возвращаем null
            return null;                  
        return (V)bucket.remove(key);
    }

    public HashMap(){
        //buckets = new Bucket[INIT_BUCKET_COUNT];
        this(INIT_BUCKET_COUNT);
    }

    public HashMap(int initCount){
        buckets = new Bucket[initCount];
    }
}
