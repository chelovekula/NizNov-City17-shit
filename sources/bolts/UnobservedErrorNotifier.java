package bolts;

import bolts.Task.UnobservedExceptionHandler;

class UnobservedErrorNotifier {
    private Task<?> task;

    public UnobservedErrorNotifier(Task<?> task2) {
        this.task = task2;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            Task<?> task2 = this.task;
            if (task2 != null) {
                UnobservedExceptionHandler unobservedExceptionHandler = Task.getUnobservedExceptionHandler();
                if (unobservedExceptionHandler != null) {
                    unobservedExceptionHandler.unobservedException(task2, new UnobservedTaskException(task2.getError()));
                }
            }
        } finally {
            super.finalize();
        }
    }

    public void setObserved() {
        this.task = null;
    }
}
