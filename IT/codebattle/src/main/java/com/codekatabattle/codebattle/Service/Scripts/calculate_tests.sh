# Calcola il numero totale di test
TOTAL_TESTS=$(find ./target/surefire-reports -type f -name '*.xml' -exec cat {} + | grep -oP '<testsuite .+? tests="\K\d+' | awk '{sum+=$1} END {print sum}')
echo "TOTAL_TESTS=$TOTAL_TESTS" >> $GITHUB_ENV

# Calcola il numero di test passati
PASSED_TESTS=$(find ./target/surefire-reports -type f -name '*.xml' -exec cat {} + | grep -oP '<testsuite .+? failures="\K\d+' | awk '{total+=$1} END {print total}' | awk -v total_tests=$TOTAL_TESTS '{print total_tests - $1}')
echo "PASSED_TESTS=$PASSED_TESTS" >> $GITHUB_ENV