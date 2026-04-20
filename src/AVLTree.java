import java.util.ArrayList;

public class AVLTree<E extends Comparable<E>>{
    protected TreeNode<E> root;
    protected int size = 0;

    public AVLTree() {
    }

    public AVLTree(E[] objects) {
        for (E object : objects) {
            insert(object);
        }
    }



    public boolean search(E e) {
        TreeNode<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }


    public boolean insert(E e) {
        if (root == null) {
            //Creates new tree node if no root
            root = new TreeNode<>(e);
        } else {
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null) {
                if (e.compareTo(current.element) < 0) {
                    parent = current;
                    current = current.left;
                } else if (e.compareTo(current.element) > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    return false;
                }
            }
            if (e.compareTo(parent.element) < 0) {
                parent.left = new TreeNode<>(e);
            } else {
                parent.right = new TreeNode<>(e);
            }
        }
        size++;
        balancePath(e);
        return true;
    }


    public boolean delete(E e) {
        if (root == null) return false;

        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                parent = current;
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                parent = current;
                current = current.right;
            } else {
                break;
            }
        }

        if (current == null) return false;

        //If node has no left child
        if (current.left == null) {
            if (parent == null) {
                root = current.right;
            } else {
                if (e.compareTo(parent.element) < 0) parent.left = current.right;
                else parent.right = current.right;
                balancePath(parent.element);
            }
        } else {

            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right;
            }

            current.element = rightMost.element;

            if (parentOfRightMost.right == rightMost) parentOfRightMost.right = rightMost.left;
            else parentOfRightMost.left = rightMost.left;

            balancePath(parentOfRightMost.element);
        }

        size--;
        return true;
    }


    private void balancePath(E e) {
        ArrayList<TreeNode<E>> path = getPath(e);
        for (int i = path.size() - 1; i >= 0; i--) {
            TreeNode<E> A = path.get(i);
            updateHeight(A);

            TreeNode<E> parentOfA = (A == root) ? null : path.get(i - 1);

            switch (balanceFactor(A)) {
                case -2:
                    if (balanceFactor(A.left) <= 0)
                        balanceLL(A, parentOfA);
                    else
                        balanceLR(A, parentOfA);
                    break;
                case 2:
                    if (balanceFactor(A.right) >= 0)
                        balanceRR(A, parentOfA);
                    else
                        balanceRL(A, parentOfA);
                    break;
            }
        }
    }

    private ArrayList<TreeNode<E>> getPath(E e) {
        ArrayList<TreeNode<E>> list = new ArrayList<>();
        TreeNode<E> current = root;
        while (current != null) {
            list.add(current);
            if (e.compareTo(current.element) < 0)
                current = current.left;
            else if (e.compareTo(current.element) > 0)
                current = current.right;
            else break;
        }
        return list;
    }

    private void updateHeight(TreeNode<E> node) {
        if (node.left == null && node.right == null)
            node.height = 0;
        else if (node.left == null)
            node.height = 1 + node.right.height;
        else if (node.right == null)
            node.height = 1 + node.left.height;
        else
            node.height = 1 + Math.max(
                    node.left.height,
                    node.right.height);
    }

    private int balanceFactor(TreeNode<E> node) {
        if (node.right == null)
            return -node.height;
        else if (node.left == null)
            return node.height;
        else
            return node.right.height - node.left.height;
    }

    private void balanceLL(TreeNode<E> A, TreeNode<E> parentOfA) {
        TreeNode<E> B = A.left;
        if (A == root)
            root = B;
        else if (parentOfA.left == A)
            parentOfA.left = B;
        else
            parentOfA.right = B;

        A.left = B.right;
        B.right = A;
        updateHeight(A);
        updateHeight(B);
    }

    private void balanceRR(TreeNode<E> A, TreeNode<E> parentOfA) {
        TreeNode<E> B = A.right;
        if (A == root)
            root = B;
        else if (parentOfA.left == A)
            parentOfA.left = B;
        else
            parentOfA.right = B;

        A.right = B.left;
        B.left = A;
        updateHeight(A);
        updateHeight(B);
    }

    private void balanceLR(TreeNode<E> A, TreeNode<E> parentOfA) {
        TreeNode<E> B = A.left;
        TreeNode<E> C = B.right;
        if (A == root)
            root = C;
        else if (parentOfA.left == A)
            parentOfA.left = C;
        else
            parentOfA.right = C;

        A.left = C.right;
        B.right = C.left;
        C.left = B;
        C.right = A;
        updateHeight(A);
        updateHeight(B);
        updateHeight(C);
    }

    private void balanceRL(TreeNode<E> A, TreeNode<E> parentOfA) {
        TreeNode<E> B = A.right;
        TreeNode<E> C = B.left;
        if (A == root)
            root = C;
        else if (parentOfA.left == A)
            parentOfA.left = C;
        else
            parentOfA.right = C;

        A.right = C.left;
        B.left = C.right;
        C.left = A;
        C.right = B;
        updateHeight(A);
        updateHeight(B);
        updateHeight(C);
    }



    public int getSize() {
        return size; }

    public void clear() {
        root = null; size = 0; }


    public void inorder() { inorder(root); }
    private void inorder(TreeNode<E> node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.element + " ");
        inorder(node.right);
    }



    public java.util.Iterator<E> iterator() {
        return new InorderIterator();
    }

    private class InorderIterator implements java.util.Iterator<E> {
        private java.util.ArrayList<E> list = new java.util.ArrayList<>();
        private int current = 0;

        public InorderIterator() {
            inorder(root);
        }

        private void inorder(TreeNode<E> node) {
            if (node == null) return;
            inorder(node.left);
            list.add(node.element);
            inorder(node.right);
        }


        public boolean hasNext() {
            return current < list.size();
        }

        public E next() {
            return list.get(current++);
        }
    }


    protected static class TreeNode<E> {
        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;
        protected int height = 0;

        public TreeNode(E e) {
            this.element = e;
        }
    }
}